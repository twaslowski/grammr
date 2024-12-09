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

  @JsonEnumDefaultValue
  UNSUPPORTED("Unsupported");

  private final String languageName;
}
