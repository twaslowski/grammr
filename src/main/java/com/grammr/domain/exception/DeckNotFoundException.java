package com.grammr.domain.exception;

public class DeckNotFoundException extends RuntimeException {

  public DeckNotFoundException(long userId) {
    super("No deck found for user " + userId);
  }
}
