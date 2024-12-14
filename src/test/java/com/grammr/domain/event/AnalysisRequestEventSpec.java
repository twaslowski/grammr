package com.grammr.domain.event;

import com.grammr.domain.entity.User;
import com.grammr.domain.entity.UserSpec;

public class AnalysisRequestEventSpec {

  public static AnalysisRequestEvent.AnalysisRequestEventBuilder valid(User user) {
    return AnalysisRequestEvent.builder()
        .phrase("Hallo Welt")
        .requestId("123")
        .userLanguageLearned(user.getLanguageLearned())
        .userLanguageSpoken(user.getLanguageSpoken())
        .performMorphologicalAnalysis(true)
        .performSemanticTranslation(true)
        .performLiteralTranslation(true);
  }
}