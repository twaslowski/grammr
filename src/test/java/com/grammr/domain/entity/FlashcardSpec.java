package com.grammr.domain.entity;

import com.grammr.domain.entity.Flashcard.Status;
import java.util.UUID;

public class FlashcardSpec {

  public static Flashcard.FlashcardBuilder withDeck(Deck deck) {
    return Flashcard.builder()
        .flashcardId(UUID.randomUUID())
        .question("What is the capital of France?")
        .answer("Paris")
        .deck(deck)
        .status(Status.CREATED);
  }
}
