package com.grammr.domain.exception;

import java.text.MessageFormat;

public class DeckNotFoundException extends RuntimeException {

  public DeckNotFoundException(String userId, long deckId) {
    super(MessageFormat.format("Deck with id {0} not found for user {1}", deckId, userId));
  }

}
