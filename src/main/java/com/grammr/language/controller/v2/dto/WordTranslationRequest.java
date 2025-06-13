package com.grammr.language.controller.v2.dto;

import com.grammr.domain.enums.LanguageCode;

public record WordTranslationRequest(
    String source,
    String context,
    LanguageCode sourceLanguage,
    LanguageCode targetLanguage
) {

}
