package com.grammr.flashcards.controller.dto;

import com.grammr.domain.enums.ExportDataType;
import jakarta.validation.constraints.NotNull;

public record DeckExportDto(
    @NotNull long deckId,
    ExportDataType exportDataType
) {

}
