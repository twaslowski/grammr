package com.grammr.language.controller.dto;

import com.grammr.domain.enums.LanguageCode;
import jakarta.validation.constraints.NotNull;

public record LiteralTranslationRequestDto(
    String phrase,
    @NotNull String word,
    @NotNull LanguageCode targetLanguage,
    LanguageCode sourceLanguage
) {

}
