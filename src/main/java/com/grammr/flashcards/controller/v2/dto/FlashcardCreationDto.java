package com.grammr.flashcards.controller.v2.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.enums.PartOfSpeechTag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record FlashcardCreationDto(
    @Schema(
        description = "The front of the flashcard, typically a question or term.",
        example = "What is the capital of France?",
        requiredMode = REQUIRED
    )
    @NotNull String question,
    @Schema(
        description = "The back of the flashcard, typically the answer or definition.",
        example = "Paris",
        requiredMode = REQUIRED
    )
    @NotNull String answer,
    @Schema(
        description = "Type of the flashcard, indicating its category or usage",
        example = "BASIC | INFLECTION",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull Flashcard.Type type,
    @Schema(
        description = "In case the flashcard is a token, this field represents the part of speech tag.",
        example = "NOUN",
        requiredMode = NOT_REQUIRED
    )
    PartOfSpeechTag tokenPos,
    @Schema(
        description = "In case the flashcard is a token, a paradigm ID can be provided to retrieve inflection tables at export time",
        example = "1",
        requiredMode = NOT_REQUIRED
    )
    UUID paradigmId
) {

}
