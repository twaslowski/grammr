package com.grammr.domain.event;

import java.util.UUID;

public record GrammaticalAnalysisRequest(
    String phrase,
    String requestId
) {

  public static GrammaticalAnalysisRequest from(String phrase) {
    return new GrammaticalAnalysisRequest(phrase, UUID.randomUUID().toString());
  }
}
