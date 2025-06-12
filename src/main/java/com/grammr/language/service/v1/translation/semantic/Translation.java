package com.grammr.language.service.v1.translation.semantic;

import com.grammr.domain.enums.LanguageCode;

public record Translation(
    String source,
    String translation,
    LanguageCode sourceLanguage,
    LanguageCode targetLanguage
) {

}
