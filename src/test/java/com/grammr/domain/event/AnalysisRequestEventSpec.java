package com.grammr.domain.event;

import com.grammr.domain.entity.User;

public class AnalysisRequestEventSpec {

  public static AnalysisRequest.AnalysisRequestBuilder valid(User user) {
    return AnalysisRequest.builder()
        .phrase("Hallo Welt")
        .requestId("123")
        .userLanguageLearned(user.getLanguageLearned())
        .userLanguageSpoken(user.getLanguageSpoken())
        .performSemanticTranslation(true);
  }
}