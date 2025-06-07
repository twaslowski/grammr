package com.grammr.flashcards.port.dto;

import com.grammr.domain.entity.Flashcard;
import java.util.List;

public record OutboundAnkiDeckExportDto(
    long deckId,
    String name,
    List<Flashcard> notes
) {

}
