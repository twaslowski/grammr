package com.grammr.flashcards.controller.v2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

/**
 * DTO used for exporting/importing a deck and its flashcards.
 * The import endpoint will create a new deck for the importing user and create new flashcards.
 */
public record DeckDumpDto(
    @Schema(description = "Original deck UUID from the exporting system", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID deckId,
    @Schema(description = "Deck name")
    String name,
    @Schema(description = "Deck description")
    String description,
    @Schema(description = "Flashcards contained in the deck")
    List<FlashcardDumpDto> flashcards
) {

  public static record FlashcardDumpDto(
      UUID flashcardId,
      String front,
      String back,
      String tokenPos,
      UUID paradigmId,
      String type
  ) {

  }
}

