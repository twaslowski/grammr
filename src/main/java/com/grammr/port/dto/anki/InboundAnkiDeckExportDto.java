package com.grammr.port.dto.anki;

import com.grammr.domain.enums.ExportDataType;
import jakarta.validation.constraints.NotNull;

public record InboundAnkiDeckExportDto(
    @NotNull long deckId,
    ExportDataType exportDataType
) {

}
