package com.grammr.service;

import com.grammr.domain.event.AnalysisRequestEvent;
import com.grammr.domain.value.FullAnalysis;
import com.grammr.domain.value.language.Token;
import com.grammr.service.language.morphology.MorphologicalAnalysisService;
import com.grammr.service.language.translation.literal.LiteralTranslationService;
import com.grammr.service.language.translation.semantic.SemanticTranslationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AnalysisRequestService {

  private static final int MAX_TOKENS = 15;

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
      var literalTranslation = literalTranslationService.translateTokens(sourcePhrase, words);
      var grammaticalAnalysis = analysisService.analyze(sourcePhrase);

      tokenService.enrichTokens(tokens, literalTranslation, grammaticalAnalysis);
    }

    return FullAnalysis.builder()
        .semanticTranslation(semanticTranslation)
        .sourcePhrase(sourcePhrase)
        .tokens(tokens)
        .build();
  }

  private boolean canAnalyze(List<Token> tokens) {
    return tokens.size() < MAX_TOKENS;
  }
}
