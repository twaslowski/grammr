package com.grammr.flashcards.port.dto;

import com.grammr.flashcards.controller.v2.dto.FlashcardDto;
import java.util.List;

public record OutboundAnkiDeckExportDto(
    long deckId,
    String name,
    String description,
    List<FlashcardDto> notes
) {

}
