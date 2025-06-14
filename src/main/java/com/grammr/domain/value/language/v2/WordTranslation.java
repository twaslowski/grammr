package com.grammr.domain.value.language.v2;

import com.grammr.domain.enums.LanguageCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record WordTranslation(
    @NotNull
    @Schema(
        description = "The source text of the word",
        example = "Hello",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String source,
    @NotNull
    @Schema(
        description = "The translated text of the word",
        example = "Hola",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String translation,
    @Schema(
        description = "The source language of the translation",
        example = "en",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    LanguageCode sourceLanguage,
    @Schema(
        description = "The target language of the translation",
        example = "es",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    LanguageCode targetLanguage,
    @Schema(
        description = "The context in which the word is used, if applicable",
        example = "Hello, how are you?",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String context
) {

}
