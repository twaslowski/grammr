package com.grammr.domain.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(long userId) {
    super("User not found with id: " + userId);
  }
}
