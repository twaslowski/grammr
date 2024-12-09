package com.grammr.domain.event;

import com.grammr.domain.entity.UserSpec;

public class AnalysisRequestEventSpec {

  public static AnalysisRequestEvent.AnalysisRequestEventBuilder valid() {
    return AnalysisRequestEvent.builder()
        .phrase("Hallo Welt")
        .requestId("123")
        .user(UserSpec.valid().build())
        .performMorphologicalAnalysis(true)
        .performSemanticTranslation(true)
        .performLiteralTranslation(true);
  }
}