package com.grammr.flashcards.controller.v2.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record DeckUpdateDto(
    @Schema(
        description = "Updated name for the deck",
        example = "Spanish Vocabulary"
    )
    String name,

    @Schema(
        description = "Updated description for the deck",
        example = "Essential Spanish vocabulary for beginners"
    )
    String description
) {

}
