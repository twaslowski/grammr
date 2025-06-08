package com.grammr.domain.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(UUID userId) {
    super("User not found with id: " + userId);
  }
}
