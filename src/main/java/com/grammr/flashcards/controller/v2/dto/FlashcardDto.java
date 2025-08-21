package com.grammr.flashcards.controller.v2.dto;

import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.entity.Flashcard.Status;
import com.grammr.domain.entity.Flashcard.Type;
import com.grammr.domain.enums.PartOfSpeechTag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.time.ZonedDateTime;
import java.util.UUID;

public record FlashcardDto(
    @Schema(
        description = "Unique identifier for the flashcard",
        example = "ff45fa93-dbcf-4b85-9994-6dcc86bada5a",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    UUID id,
    @Schema(
        description = "Front side of the flashcard, typically a question or term",
        example = "What is the capital of France?",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String question,
    @Schema(
        description = "Back side of the flashcard, typically the answer or definition",
        example = "Paris",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String answer,
    @Schema(
        description = "Current status of the flashcard, indicating its lifecycle stage",
        example = "CREATED",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    Status status,
    @Schema(
        description = "Type of the flashcard, indicating its category or usage",
        example = "BASIC | INFLECTION",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    Type type,
    @Schema(
        description = """
            If the flashcard is associated with a single word, this field contains the part of speech tag.
            Required if type == 'INFLECTION'.
            """,
        example = "NOUN",
        requiredMode = RequiredMode.NOT_REQUIRED
    )
    PartOfSpeechTag tokenPos,
    @Schema(
        description = """
            Unique identifier for the paradigm associated with the flashcard, if applicable.
            Required if type == 'INFLECTION'.
            """,
        example = "123e4567-e89b-12d3-a456-426614174000",
        requiredMode = RequiredMode.NOT_REQUIRED
    )
    String paradigmId,
    @Schema(
        description = "Timestamp when the flashcard was created",
        example = "CREATED",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    ZonedDateTime createdTimestamp,
    @Schema(
        description = "Timestamp when the flashcard was last updated",
        example = "UPDATED",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    ZonedDateTime updatedTimestamp
) {

  public static FlashcardDto fromEntity(Flashcard flashcard) {
    return new FlashcardDto(
        flashcard.getFlashcardId(),
        flashcard.getFront(),
        flashcard.getBack(),
        flashcard.getStatus(),
        flashcard.getType(),
        flashcard.getTokenPos(),
        flashcard.getParadigmId(),
        flashcard.getCreatedTimestamp(),
        flashcard.getUpdatedTimestamp()
    );
  }
}
