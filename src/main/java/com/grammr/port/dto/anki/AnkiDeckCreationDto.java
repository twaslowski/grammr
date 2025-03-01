package com.grammr.port.dto.anki;

import jakarta.validation.constraints.NotNull;

public record AnkiDeckCreationDto(
    @NotNull String name,
    String description
) {

}
