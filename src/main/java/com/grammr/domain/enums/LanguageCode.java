package com.grammr.domain.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LanguageCode {

  RU("Russian"),
  EN("English"),
  DE("German"),
  IT("Italian"),
  ES("Spanish"),
  PT("Portuguese"),
  FR("French"),

  @JsonEnumDefaultValue
  UNSUPPORTED("Unsupported");

  private final String languageName;
}
