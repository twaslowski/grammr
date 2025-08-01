package com.grammr.flashcards.controller.v2.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.grammr.domain.entity.Deck;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record DeckDto(
    @Schema(
        description = "Unique identifier of the deck",
        example = "123e4567-e89b-12d3-a456-426614174000",
        requiredMode = REQUIRED
    )
    UUID id,
    @Schema(
        description = "Name of the deck",
        example = "My Flashcards",
        requiredMode = REQUIRED
    )
    String name,
    @Schema(
        description = "Description of the deck",
        example = "A collection of my favorite flashcards",
        requiredMode = REQUIRED
    )
    String description,
    @Schema(
        description = "Timestamp when the deck was created",
        example = "2023-10-01T12:00:00Z",
        requiredMode = REQUIRED
    )
    ZonedDateTime createdTimestamp,
    @Schema(
        description = "Timestamp when the deck was last updated",
        example = "2023-10-02T12:00:00Z",
        requiredMode = REQUIRED
    )
    ZonedDateTime updatedTimestamp
) {

  public static DeckDto from(Deck deck) {
    return new DeckDto(
        deck.getDeckId(),
        deck.getName(),
        deck.getDescription(),
        deck.getCreatedTimestamp(),
        deck.getUpdatedTimestamp()
    );
  }
}
