package com.grammr.domain.event;

import com.grammr.domain.enums.LanguageCode;
import lombok.Builder;

@Builder
public record AnalysisRequestEvent(
    String phrase,
    String requestId,
    LanguageCode userLanguageSpoken,
    LanguageCode userLanguageLearned,
    boolean performSemanticTranslation,
    boolean performLiteralTranslation,
    boolean performMorphologicalAnalysis
) {

  public static AnalysisRequestEvent.AnalysisRequestEventBuilder full() {
    return AnalysisRequestEvent.builder()
        .performMorphologicalAnalysis(true)
        .performLiteralTranslation(true)
        .performSemanticTranslation(true);
  }

  // todo: workaround; eventually the AnalysisService should determine this itself
  public AnalysisRequestEvent withoutLanguageInformation() {
    return AnalysisRequestEvent.builder()
        .phrase(phrase)
        .requestId(requestId)
        .performSemanticTranslation(performSemanticTranslation)
        .performLiteralTranslation(false)
        .performMorphologicalAnalysis(false)
        .build();
  }
}
