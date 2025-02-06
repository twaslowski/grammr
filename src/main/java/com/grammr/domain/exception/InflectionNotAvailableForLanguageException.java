package com.grammr.domain.exception;

import com.grammr.domain.enums.LanguageCode;

public class InflectionNotAvailableForLanguageException extends RuntimeException {

  public InflectionNotAvailableForLanguageException(LanguageCode languageCode) {
    super("Inflection is not available for language: " + languageCode);
  }
}
