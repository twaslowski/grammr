package com.grammr.telegram.exception;

public class UpdateNotProcessableException extends RuntimeException {

  public UpdateNotProcessableException(Throwable throwable) {
    super(throwable);
  }
}
