package com.grammr.domain.exception;

public class RequestNotFoundException extends RuntimeException {

  public RequestNotFoundException(String requestId) {
    super(String.format("Request not found: %s", requestId));
  }
}
