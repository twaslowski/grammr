package com.grammr.service;

import com.grammr.domain.event.FullAnalysisRequest;
import com.grammr.domain.value.FullAnalysis;
import com.grammr.language.analysis.MorphologicalAnalysisService;
import com.grammr.language.translation.literal.LiteralTranslationService;
import com.grammr.language.translation.semantic.SemanticTranslationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AnalysisRequestService {

  private final SemanticTranslationService semanticTranslationService;
  private final LiteralTranslationService literalTranslationService;
  private final MorphologicalAnalysisService analysisService;

  public FullAnalysis processFullAnalysisRequest(FullAnalysisRequest analysisRequest) {
    var sourcePhrase = analysisRequest.phrase();
    log.info("Processing analysis request for source phrase: '{}'", sourcePhrase);
    var semanticTranslation = semanticTranslationService.createSemanticTranslation(sourcePhrase);
    var literalTranslation = literalTranslationService.createLiteralTranslation(sourcePhrase);
    var grammaticalAnalysis = analysisService.analyze(sourcePhrase);
    return FullAnalysis.builder()
        .literalTranslation(literalTranslation)
        .semanticTranslation(semanticTranslation)
        .morphologicalAnalysis(grammaticalAnalysis)
        .sourcePhrase(sourcePhrase)
        .build();
  }
}
