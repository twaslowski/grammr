package com.grammr.domain.exception;

import static java.lang.String.format;

public class UserAlreadyExistsException extends RuntimeException {

  public UserAlreadyExistsException(String email) {
    super(format("User with email %s already exists", email));
  }
}
