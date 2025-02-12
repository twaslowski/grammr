package com.grammr.port.dto.anki;

public record AnkiFlashcardCreationDto(
    long deckId,
    String question,
    String answer
) {

}
