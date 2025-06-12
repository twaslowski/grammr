package com.grammr.language.controller.v1.dto;

import com.grammr.domain.enums.LanguageCode;
import java.util.UUID;

public record MorphologicalAnalysisRequest(
    String phrase,
    LanguageCode languageCode,
    String requestId
) {

  public static MorphologicalAnalysisRequest from(String phrase, LanguageCode languageCode) {
    return new MorphologicalAnalysisRequest(phrase, languageCode, UUID.randomUUID().toString());
  }
}
