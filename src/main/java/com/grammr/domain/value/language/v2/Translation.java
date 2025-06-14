package com.grammr.domain.value.language.v2;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.language.controller.v2.dto.PhraseAnalysis;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

public record Translation(
    @NotNull
    @Schema(
        description = "The original phrase in the source language.",
        example = "Hello, how are you?"
    )
    String source,
    @Schema(
        description = "The translated phrase in the target language.",
        example = "Hola, ¿cómo estás?"
    )
    @NotNull String translation,
    @Schema(
        description = "The language code of the source language.",
        example = "en"
    )
    @NotNull LanguageCode sourceLanguage,
    @Schema(
        description = "The language code of the target language.",
        example = "es"
    )
    @NotNull LanguageCode targetLanguage,
    @Schema(
        description = """
            Analysis of the translated phrase, supplied if performAnalysis was set to
            true in the request.
            """
    )
    @Nullable PhraseAnalysis analysis
) {

  public Translation withAnalysis(PhraseAnalysis analysis) {
    return new Translation(
        source,
        translation,
        sourceLanguage,
        targetLanguage,
        analysis
    );
  }
}
