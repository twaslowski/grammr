package com.grammr.port.dto.anki;

import com.grammr.domain.enums.PartOfSpeechTag;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AnkiFlashcardCreationDto(
    @NotNull long deckId,
    @NotNull String question,
    @NotNull String answer,
    PartOfSpeechTag tokenPos,
    UUID paradigmId
) {

}
