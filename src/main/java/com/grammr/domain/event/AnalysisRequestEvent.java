package com.grammr.domain.event;

import com.grammr.domain.User;
import com.grammr.domain.enums.LanguageCode;
import lombok.Builder;

@Builder
public record AnalysisRequestEvent(
    String phrase,
    String requestId,
    User user,
    boolean performSemanticTranslation,
    boolean performLiteralTranslation,
    boolean performMorphologicalAnalysis
) {

  public LanguageCode getUserLanguageSpoken() {
    return user.getLanguageSpoken();
  }

  public LanguageCode getUserLanguageLearned() {
    return user.getLanguageLearned();
  }
}
