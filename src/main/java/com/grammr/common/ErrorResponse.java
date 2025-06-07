package com.grammr.common;

public record ErrorResponse(
    String message
) {

  public static ErrorResponse withMessage(String message) {
    return new ErrorResponse(message);
  }

}
