package com.grammr.domain.exception;

public class ConfigurationNotAvailableException extends BadRequestException {

  public ConfigurationNotAvailableException(String message) {
    super(message);
  }
}
