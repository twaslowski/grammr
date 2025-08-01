package com.grammr.flashcards.controller.v2.dto;

import jakarta.validation.constraints.NotNull;

public record DeckCreationDto(
    @NotNull String name,
    String description
) {

}
