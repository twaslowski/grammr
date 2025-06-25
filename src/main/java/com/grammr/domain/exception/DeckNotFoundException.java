package com.grammr.domain.exception;

import java.util.UUID;

public class DeckNotFoundException extends ResourceNotFoundException {

  public DeckNotFoundException(UUID deckId) {
    super(deckId.toString());
  }
}
