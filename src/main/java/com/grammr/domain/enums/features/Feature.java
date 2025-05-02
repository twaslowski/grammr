package com.grammr.domain.enums.features;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Feature(FeatureType type, String value) {

  @JsonIgnore
  public FeatureProperty getEnumValue() {
    return switch (type) {
      case GENDER -> Gender.valueOf(value);
      case CASE -> Case.valueOf(value);
      case NUMBER -> Number.valueOf(value);
      case PERSON -> Person.from(value);
      case TENSE -> Tense.valueOf(value);
      case ANIMACY -> Animacy.valueOf(value);
      case MISC -> MiscellaneousFeature.UNSUPPORTED_FEATURE;
    };
  }

  @JsonProperty("fullIdentifier")
  public String fullIdentifier() {
    return getEnumValue().fullIdentifier();
  }

  public static Feature of(FeatureType type, String value) {
    return new Feature(type, value);
  }
}