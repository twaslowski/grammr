package com.grammr.domain.event;

import com.grammr.domain.enums.LanguageCode;

public class AnalysisRequestEventSpec {

  public static AnalysisRequest.AnalysisRequestBuilder valid() {
    return AnalysisRequest.builder()
        .phrase("Hallo Welt")
        .requestId("123")
        .userLanguageLearned(LanguageCode.DE)
        .userLanguageSpoken(LanguageCode.EN)
        .performSemanticTranslation(true);
  }
}