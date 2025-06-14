package com.grammr.language.controller.v2.dto;

import com.grammr.domain.enums.LanguageCode;

public record AnalysisRequest(
    String phrase,
    LanguageCode language
) {

}
