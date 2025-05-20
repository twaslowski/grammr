package com.grammr.controller.dto;

import jakarta.validation.constraints.NotNull;

public record DeckCreationDto(
    @NotNull String name,
    String description
) {

}
