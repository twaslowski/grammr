package com.grammr.controller.dto;

import com.grammr.domain.entity.Deck;
import com.grammr.domain.entity.Flashcard;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record DeckDto(
    long id,
    String name,
    String description,
    ZonedDateTime createdTimestamp,
    ZonedDateTime updatedTimestamp,
    List<Flashcard> flashcards
) {

  public DeckDto(Deck deck, List<Flashcard> flashcards) {
    this(deck.getId(), deck.getName(), deck.getDescription(), deck.getCreatedTimestamp(), deck.getUpdatedTimestamp(), flashcards);
  }
}
