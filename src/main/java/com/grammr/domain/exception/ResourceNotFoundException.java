package com.grammr.domain.exception;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String id) {
    super("Resource with id %s not found".formatted(id));
  }
}
