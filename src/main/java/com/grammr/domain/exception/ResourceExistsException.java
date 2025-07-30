package com.grammr.domain.exception;

public class ResourceExistsException extends RuntimeException {

  public ResourceExistsException(String message) {
    super(message);
  }
}
