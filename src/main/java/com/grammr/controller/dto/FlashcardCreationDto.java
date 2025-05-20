package com.grammr.controller.dto;

import com.grammr.domain.enums.PartOfSpeechTag;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record FlashcardCreationDto(
    @NotNull long deckId,
    @NotNull String question,
    @NotNull String answer,
    PartOfSpeechTag tokenPos,
    UUID paradigmId
) {

}
