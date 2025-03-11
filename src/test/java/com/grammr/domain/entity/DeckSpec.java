package com.grammr.domain.entity;

public class DeckSpec {

  public static Deck.DeckBuilder withUser(User user) {
    return Deck.builder()
        .id(1L)
        .user(user)
        .name("some deck name")
        .description("some description");
  }
}
