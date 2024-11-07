package com.grammr.service;

import com.grammr.domain.event.AnalysisRequestEvent;
import com.grammr.domain.value.Analysis;
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

  public Analysis processAnalysisRequest(AnalysisRequestEvent analysisRequest) {
    var sourcePhrase = analysisRequest.phrase();
    log.info("Processing analysis request for source phrase: '{}'", sourcePhrase);
    var semanticTranslation = semanticTranslationService.createSemanticTranslation(sourcePhrase);
    var literalTranslation = literalTranslationService.createLiteralTranslation(sourcePhrase);
    return Analysis.builder()
        .literalTranslation(literalTranslation)
        .semanticTranslation(semanticTranslation)
        .sourcePhrase(sourcePhrase)
        .build();
  }
}
