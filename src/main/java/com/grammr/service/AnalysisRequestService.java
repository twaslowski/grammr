package com.grammr.service;

import com.grammr.domain.event.AnalysisRequestEvent;
import com.grammr.domain.value.FullAnalysis;
import com.grammr.domain.value.language.MorphologicalAnalysis;
import com.grammr.domain.value.language.Token;
import com.grammr.domain.value.language.TokenTranslation;
import com.grammr.service.language.morphology.MorphologicalAnalysisService;
import com.grammr.service.language.translation.literal.LiteralTranslationService;
import com.grammr.service.language.translation.semantic.SemanticTranslationService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AnalysisRequestService {

  private static final int MAX_TOKENS = 15;

  private final ExecutorService executorService = Executors.newCachedThreadPool();

  private final SemanticTranslationService semanticTranslationService;
  private final LiteralTranslationService literalTranslationService;
  private final MorphologicalAnalysisService analysisService;
  private final TokenService tokenService;

  public FullAnalysis processFullAnalysisRequest(AnalysisRequestEvent analysisRequest) {
    var sourcePhrase = analysisRequest.phrase();
    log.info("Processing analysis request for source phrase: '{}'", sourcePhrase);

    var tokens = tokenService.tokenize(sourcePhrase);
    var words = tokens.stream().map(Token::text).toList();

    var semanticTranslation = semanticTranslationService.createSemanticTranslation(sourcePhrase);

    if (canAnalyze(tokens)) {
      log.info("Analyzing source phrase: '{}'", sourcePhrase);

      var literalTranslationFuture = createLiteralTranslationFuture(sourcePhrase, words);
      var grammaticalAnalysisFuture = createGrammaticalAnalysisFuture(sourcePhrase);

      var literalTranslation = literalTranslationFuture.join();
      var grammaticalAnalysis = grammaticalAnalysisFuture.join();

      tokens = tokenService.enrichTokens(tokens, literalTranslation, grammaticalAnalysis);

      return FullAnalysis.builder()
          .semanticTranslation(semanticTranslation)
          .sourcePhrase(sourcePhrase)
          .tokens(tokens)
          .build();
    }

    return FullAnalysis.builder()
        .semanticTranslation(semanticTranslation)
        .sourcePhrase(sourcePhrase)
        .tokens(List.of())
        .build();
  }

  private boolean canAnalyze(List<Token> tokens) {
    return tokens.size() < MAX_TOKENS;
  }

  private CompletableFuture<List<TokenTranslation>> createLiteralTranslationFuture(String sourcePhrase, List<String> words) {
    return CompletableFuture.supplyAsync(() -> literalTranslationService.translateTokens(sourcePhrase, words), executorService);
  }

  private CompletableFuture<MorphologicalAnalysis> createGrammaticalAnalysisFuture(String sourcePhrase) {
    return CompletableFuture.supplyAsync(() -> analysisService.analyze(sourcePhrase), executorService);
  }
}
