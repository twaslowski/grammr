package com.grammr.domain.exception;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.enums.PartOfSpeechTag;
import java.text.MessageFormat;

public class InflectionNotAvailableException extends BadRequestException {

  private static final String NOT_AVAILABLE_FOR_LANGUAGE = "Inflections are not available in {0}";
  private static final String NOT_AVAILABLE_FOR_PART_OF_SPEECH = "Inflections are not available for {0}s in {1}";

  public InflectionNotAvailableException(LanguageCode languageCode) {
    super(MessageFormat.format(NOT_AVAILABLE_FOR_LANGUAGE, languageCode.getLanguageName()));
  }

  public InflectionNotAvailableException(LanguageCode languageCode, PartOfSpeechTag partOfSpeechTag) {
    super(MessageFormat.format(NOT_AVAILABLE_FOR_PART_OF_SPEECH, partOfSpeechTag.getFullIdentifier().toLowerCase(), languageCode.getLanguageName()));
  }
}
