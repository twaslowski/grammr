package com.grammr.domain.event;

import java.util.UUID;

public record MorphologicalAnalysisRequest(
    String phrase,
    String requestId
) {

  public static MorphologicalAnalysisRequest from(String phrase) {
    return new MorphologicalAnalysisRequest(phrase, UUID.randomUUID().toString());
  }
}
