package com.grammr.domain.enums.features;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum FeatureType {
  CASE,
  NUMBER,
  PERSON,
  TENSE,
  GENDER,
  ANIMACY,

  @JsonEnumDefaultValue
  MISC
}
