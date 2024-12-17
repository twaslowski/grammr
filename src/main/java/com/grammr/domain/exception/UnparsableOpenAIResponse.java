package com.grammr.domain.exception;

public class UnparsableOpenAIResponse extends RuntimeException {

  public UnparsableOpenAIResponse(String message, Throwable cause) {
    super(message, cause);
  }
}
