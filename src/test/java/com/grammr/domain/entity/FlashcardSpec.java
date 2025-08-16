package com.grammr.domain.entity;

import com.grammr.domain.entity.Flashcard.Status;
import com.grammr.domain.entity.Flashcard.Type;
import java.util.UUID;

public class FlashcardSpec {

  public static Flashcard.FlashcardBuilder withDeck(Deck deck) {
    return Flashcard.builder()
        .flashcardId(UUID.randomUUID())
        .front("What is the capital of France?")
        .back("Paris")
        .type(Type.BASIC)
        .deck(deck)
        .status(Status.CREATED);
  }
}
