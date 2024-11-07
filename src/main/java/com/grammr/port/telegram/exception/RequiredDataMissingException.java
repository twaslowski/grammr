package com.grammr.port.telegram.exception;

public class RequiredDataMissingException extends RuntimeException {

  public RequiredDataMissingException(Throwable throwable) {
    super(throwable);
  }
}
