package com.grammr.domain.exception;

import static java.lang.String.format;

public class LanguageNotAvailableException extends RuntimeException {

  public LanguageNotAvailableException(String languageCode) {
    super(format("Language %s is not available yet", languageCode));
  }
}
