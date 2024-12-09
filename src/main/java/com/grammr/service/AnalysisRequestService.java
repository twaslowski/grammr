package com.grammr.service;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.event.AnalysisRequestEvent;
import com.grammr.domain.value.FullAnalysis;
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

    var user = analysisRequest.user();

    var sourceLanguage = languageRecognitionService.recognizeLanguage(sourcePhrase);

    if (sourceLanguage.getLanguageCode().equals(user.getLanguageLearned())) {
      return createFullAnalysisForLearnedLanguage(analysisRequest, sourceLanguage.getLanguageCode());
    } else if (sourceLanguage.getLanguageCode().equals(user.getLanguageSpoken())) {
      return createFullAnalysisForSpokenLanguage(analysisRequest);
    } else {
      return FullAnalysis.builder()
          .sourcePhrase(sourcePhrase)
          .build();
    }
  }

  private FullAnalysis createFullAnalysisForLearnedLanguage(AnalysisRequestEvent event,
                                                            LanguageCode languageCode) {
    var phrase = event.phrase();
    var tokens = tokenService.tokenize(phrase);
    var words = tokens.stream().map(Token::text).toList();

    var semanticTranslationFuture = supplyAsync(() ->
        semanticTranslationService.createSemanticTranslation(phrase,
            event.user().getLanguageLearned(),
            event.user().getLanguageSpoken()
        ));

    if (tokens.size() < MAX_TOKENS) {
      var literalTranslationFuture = supplyAsync(() -> literalTranslationService.translateTokens(phrase, words));
      var grammaticalAnalysisFuture = supplyAsync(() -> analysisService.analyze(phrase, languageCode));
      awaitAll(literalTranslationFuture, grammaticalAnalysisFuture);

      var literalTranslation = join(literalTranslationFuture);
      var grammaticalAnalysis = join(grammaticalAnalysisFuture);
      tokens = tokenService.enrichTokens(tokens, literalTranslation, grammaticalAnalysis);
    } else {
      log.info("Skipping literal translation and grammatical analysis for phrase '{}'", phrase);
      tokens = List.of();
    }

    var semanticTranslation = join(semanticTranslationFuture);

    return FullAnalysis.builder()
        .semanticTranslation(semanticTranslation)
        .sourcePhrase(phrase)
        .analyzedTokens(tokens)
        .build();
  }

  private FullAnalysis createFullAnalysisForSpokenLanguage(AnalysisRequestEvent event) {
    var translationIntoLearnedLanguage = semanticTranslationService.createSemanticTranslation(
        event.phrase(), event.user().getLanguageSpoken(), event.user().getLanguageLearned()
    );
    var newEvent = AnalysisRequestEvent.builder()
        .phrase(translationIntoLearnedLanguage.getTranslatedPhrase())
        .user(event.user())
        .build();
    return createFullAnalysisForLearnedLanguage(newEvent, event.user().getLanguageLearned());
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
