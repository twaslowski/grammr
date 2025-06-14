package com.grammr.domain.exception;

@Deprecated
public class UnparsableOpenAIResponse extends RuntimeException {

  public UnparsableOpenAIResponse(String message, Throwable cause) {
    super(message, cause);
  }
}
