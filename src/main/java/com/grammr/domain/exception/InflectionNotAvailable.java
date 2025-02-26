package com.grammr.domain.exception;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.enums.PartOfSpeechTag;
import java.text.MessageFormat;

public class InflectionNotAvailable extends RuntimeException {

  public InflectionNotAvailable(LanguageCode languageCode) {
    super("Inflection is not available for language: " + languageCode);
  }

  public InflectionNotAvailable(PartOfSpeechTag partOfSpeechTag) {
    super(MessageFormat.format("Inflections are not available for {0}s", partOfSpeechTag.getFullIdentifier()));
  }
}
