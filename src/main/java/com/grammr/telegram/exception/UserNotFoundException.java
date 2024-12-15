package com.grammr.telegram.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(long chatId) {
    super(String.format("User with chatId %d not found", chatId));
  }
}
