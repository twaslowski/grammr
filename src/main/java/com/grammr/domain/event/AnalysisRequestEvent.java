package com.grammr.domain.event;

import com.grammr.domain.entity.User;
import com.grammr.domain.enums.LanguageCode;
import lombok.Builder;

@Builder
public record AnalysisRequestEvent(
    String phrase,
    String requestId,
    // todo user should be removed eventually, I think?
    User user,
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

  public LanguageCode getUserLanguageSpoken() {
    return user.getLanguageSpoken();
  }

  public LanguageCode getUserLanguageLearned() {
    return user.getLanguageLearned();
  }
}
