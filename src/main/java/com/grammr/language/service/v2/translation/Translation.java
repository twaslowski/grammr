package com.grammr.language.service.v2.translation;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.language.controller.v2.dto.PhraseAnalysis;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

public record Translation(
    @NotNull String source,
    @NotNull String translation,
    @NotNull LanguageCode sourceLanguage,
    @NotNull LanguageCode targetLanguage,
    @Nullable PhraseAnalysis translationAnalysis
) {

}
