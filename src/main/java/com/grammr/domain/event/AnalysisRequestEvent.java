package com.grammr.domain.event;

import com.grammr.domain.enums.LanguageCode;

public record AnalysisRequestEvent(
    LanguageCode languageCode,
    String phrase,
    String requestId
) {

}
