package com.grammr.language.controller.v2.dto;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.language.Token;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
public record PhraseAnalysis(
    @NotNull
    @Schema(
        description = "Unique identifier for the analysis",
        example = "123e4567-e89b-12d3-a456-426614174000"
    )
    String analysisId,
    @NotNull
    @Schema(
        description = "The phrase that was analyzed",
        example = "Wie geht es dir?"
    )
    String phrase,
    @NotNull
    @Schema(
        description = "The source language of the phrase",
        example = "DE"
    )
    LanguageCode sourceLanguage,
    @NotNull
    @Schema(
        description = "List of tokens with their morphological analysis",
        implementation = Token.class
    )
    List<Token> analysedTokens
) {

}
