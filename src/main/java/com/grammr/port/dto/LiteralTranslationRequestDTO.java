package com.grammr.port.dto;

import com.grammr.domain.enums.LanguageCode;
import jakarta.validation.constraints.NotNull;

public record LiteralTranslationRequestDTO (
    String phrase,
    @NotNull String word,
    @NotNull LanguageCode targetLanguage,
    LanguageCode sourceLanguage
) {

}
