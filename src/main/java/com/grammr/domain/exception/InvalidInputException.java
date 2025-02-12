package com.grammr.domain.exception;

public class InvalidInputException extends Throwable {

  public InvalidInputException(String input) {
    super("Invalid input: " + input);
  }
}
