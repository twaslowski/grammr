package com.grammr.domain.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum LanguageCode {

  RU,
  EN,
  DE,

  @JsonEnumDefaultValue
  UNSUPPORTED
}
