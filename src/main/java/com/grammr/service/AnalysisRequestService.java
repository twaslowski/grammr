package com.grammr.service;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.event.AnalysisRequestEvent;
import com.grammr.domain.value.FullAnalysis;
import com.grammr.domain.value.language.LanguageRecognition;
import com.grammr.domain.value.language.Token;
import com.grammr.service.language.morphology.MorphologicalAnalysisService;
import com.grammr.service.language.recognition.LanguageRecognitionService;
import com.grammr.service.language.translation.literal.LiteralTranslationService;
import com.grammr.service.language.translation.semantic.SemanticTranslationService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Builder
@Slf4j
public class AnalysisRequestService {

  private static final int MAX_TOKENS = 15;

  private final ExecutorService executorService = Executors.newCachedThreadPool();

  private final SemanticTranslationService semanticTranslationService;
  private final LanguageRecognitionService languageRecognitionService;
  private final LiteralTranslationService literalTranslationService;
  private final MorphologicalAnalysisService analysisService;
  private final TokenService tokenService;

  public FullAnalysis processFullAnalysisRequest(AnalysisRequestEvent analysisRequest) {
    var sourcePhrase = analysisRequest.phrase();
    log.info("Processing analysis request for source phrase: '{}'", sourcePhrase);

    var tokens = tokenService.tokenize(sourcePhrase);
    var words = tokens.stream().map(Token::text).toList();

    var languageRecognitionFuture = supplyAsync(() -> languageRecognitionService.recognizeLanguage(sourcePhrase));
    var semanticTranslationFuture = supplyAsync(() -> semanticTranslationService.createSemanticTranslation(sourcePhrase));
    awaitAll(languageRecognitionFuture, semanticTranslationFuture);

    var languageRecognition = join(languageRecognitionFuture);
    var semanticTranslation = join(semanticTranslationFuture);

    if (canAnalyze(tokens, languageRecognition)) {
      log.info("Analyzing source phrase: '{}'", sourcePhrase);

      var literalTranslationFuture = supplyAsync(() -> literalTranslationService.translateTokens(sourcePhrase, words));
      var grammaticalAnalysisFuture = supplyAsync(() -> analysisService.analyze(sourcePhrase, languageRecognition.getLanguageCode()));
      awaitAll(literalTranslationFuture, grammaticalAnalysisFuture);

      var literalTranslation = join(literalTranslationFuture);
      var grammaticalAnalysis = join(grammaticalAnalysisFuture);

      tokens = tokenService.enrichTokens(tokens, literalTranslation, grammaticalAnalysis);

      return FullAnalysis.builder()
          .semanticTranslation(semanticTranslation)
          .languageRecognition(languageRecognition)
          .sourcePhrase(sourcePhrase)
          .analyzedTokens(tokens)
          .build();
    }

    return FullAnalysis.builder()
        .semanticTranslation(semanticTranslation)
        .languageRecognition(languageRecognition)
        .sourcePhrase(sourcePhrase)
        .analyzedTokens(List.of())
        .build();
  }

  private boolean canAnalyze(List<Token> tokens, LanguageRecognition languageRecognition) {
    return tokens.size() < MAX_TOKENS && languageRecognition.getLanguageCode() != LanguageCode.UNSUPPORTED;
  }

  public <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
    return CompletableFuture.supplyAsync(supplier, executorService);
  }

  public static void awaitAll(CompletableFuture<?>... futures) {
    CompletableFuture.allOf(futures);
  }

  public static <T> T join(CompletableFuture<T> future) {
    return future.join();
  }
}
