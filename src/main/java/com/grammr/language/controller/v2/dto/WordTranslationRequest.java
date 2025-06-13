package com.grammr.language.controller.v2.dto;

import com.grammr.domain.enums.LanguageCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record WordTranslationRequest(
    @NotNull
    @Schema(
        description = "The word to translate",
        example = "Bank"
    )
    String source,
    @NotNull
    @Schema(
        description = "The context in which the word is used, to help disambiguate meanings",
        example = "Ich gehe nachher zur Bank, um Geld abzuheben."
    )
    String context,
    @NotNull
    @Schema(
        description = "The target language to translate the word into",
        example = "EN"
    )
    LanguageCode targetLanguage
) {

}
