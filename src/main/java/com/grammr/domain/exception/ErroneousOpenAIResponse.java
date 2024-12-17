package com.grammr.domain.exception;

public class ErroneousOpenAIResponse extends RuntimeException {

  public ErroneousOpenAIResponse(Throwable cause) {
    super(cause);
  }
}
