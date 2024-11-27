package com.grammr.domain.event;

public class AnalysisRequestEventSpec {

  public static AnalysisRequestEvent.AnalysisRequestEventBuilder valid() {
    return AnalysisRequestEvent.builder()
        .phrase("Hallo Welt")
        .requestId("123")
        .chatId(123);
  }
}