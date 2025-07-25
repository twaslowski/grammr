package com.grammr.flashcards.controller.dto;

import com.grammr.domain.enums.PartOfSpeechTag;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record FlashcardCreationDto(
    @NotNull UUID deckId,
    @NotNull String question,
    @NotNull String answer,
    PartOfSpeechTag tokenPos,
    UUID paradigmId
) {

}
