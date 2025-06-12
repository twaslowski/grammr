package com.grammr.language.controller.v2.dto;

import com.grammr.domain.enums.LanguageCode;
import jakarta.validation.constraints.NotNull;

public record PhraseTranslationRequest(
    @NotNull String phrase,
    @NotNull LanguageCode targetLanguage,
    @NotNull LanguageCode sourceLanguage,
    boolean performAnalysis
) {

}
