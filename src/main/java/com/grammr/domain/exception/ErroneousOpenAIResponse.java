package com.grammr.domain.exception;

@Deprecated
public class ErroneousOpenAIResponse extends RuntimeException {

  public ErroneousOpenAIResponse(Throwable cause) {
    super(cause);
  }
}
