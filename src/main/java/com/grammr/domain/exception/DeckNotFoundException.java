package com.grammr.domain.exception;

import java.text.MessageFormat;

public class DeckNotFoundException extends RuntimeException {

  public DeckNotFoundException(long userId, long deckId) {
    super(MessageFormat.format("Deck with id {0} not found for user {1}", deckId, userId));
  }
}
