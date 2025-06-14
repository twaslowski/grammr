package com.grammr.domain.exception;

import java.util.UUID;

public class UserNotFoundException extends ResourceNotFoundException {

  public UserNotFoundException(UUID userId) {
    super("User not found with id: " + userId);
  }
}
