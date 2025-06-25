package com.grammr.flashcards.controller.dto;

import com.grammr.domain.enums.ExportDataType;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record DeckExportDto(
    @NotNull UUID deckId,
    ExportDataType exportDataType
) {

}
