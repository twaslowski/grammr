package com.grammr.port.inbound;

public record ErrorResponse(
    String message
) {

  public static ErrorResponse withMessage(String message) {
    return new ErrorResponse(message);
  }

}
