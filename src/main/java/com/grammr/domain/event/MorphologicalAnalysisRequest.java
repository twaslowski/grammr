package com.grammr.domain.event;

import com.grammr.domain.enums.LanguageCode;
import java.util.UUID;

public record MorphologicalAnalysisRequest(
    String phrase,
    String languageCode,
    String requestId
) {

  public static MorphologicalAnalysisRequest from(String phrase, LanguageCode languageCode) {
    return new MorphologicalAnalysisRequest(phrase, languageCode.toString(), UUID.randomUUID().toString());
  }
}
