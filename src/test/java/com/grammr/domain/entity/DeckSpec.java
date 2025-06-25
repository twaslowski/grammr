package com.grammr.domain.entity;

import java.util.UUID;

public class DeckSpec {

  public static Deck.DeckBuilder withUser(User user) {
    return Deck.builder()
        .deckId(UUID.randomUUID())
        .owner(user)
        .name("some deck name")
        .description("some description");
  }
}
