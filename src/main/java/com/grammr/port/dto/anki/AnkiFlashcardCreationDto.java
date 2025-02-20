package com.grammr.port.dto.anki;

import jakarta.validation.constraints.NotNull;

public record AnkiFlashcardCreationDto(
    @NotNull long deckId,
    @NotNull String question,
    @NotNull String answer
) {

}
