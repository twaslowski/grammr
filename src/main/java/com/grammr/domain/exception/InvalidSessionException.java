package com.grammr.domain.exception;

public class InvalidSessionException extends RuntimeException {

  public InvalidSessionException(String sessionId) {
    super("Invalid session: " + sessionId);
  }
}
