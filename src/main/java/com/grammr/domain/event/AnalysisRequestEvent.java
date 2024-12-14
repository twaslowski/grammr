package com.grammr.domain.event;

import com.grammr.domain.entity.User;
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

  public AnalysisRequestEvent withLanguageInformation(User user) {
    return AnalysisRequestEvent.builder()
        .phrase(phrase)
        .requestId(requestId)
        .userLanguageLearned(user.getLanguageLearned())
        .userLanguageSpoken(user.getLanguageSpoken())
        .performSemanticTranslation(performSemanticTranslation)
        .performLiteralTranslation(performLiteralTranslation)
        .performMorphologicalAnalysis(performMorphologicalAnalysis)
        .build();
  }
}
