package com.grammr.telegram.exception;

public class RequiredDataMissingException extends RuntimeException {

  public RequiredDataMissingException(Throwable throwable) {
    super(throwable);
  }
}
