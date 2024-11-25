package com.grammr.domain.enums.features;

public record Feature(FeatureType type, String value) {

  // todo:  Include validation at deserialization time instead of retrieval.
  public Enum<? extends FeatureProperty> getEnumValue() {
    return switch (type) {
      case GENDER -> Gender.valueOf(value);
      case CASE -> Case.valueOf(value);
      case NUMBER -> Number.valueOf(value);
      case PERSON -> Person.valueOf(value);
      case TENSE -> Tense.valueOf(value);
      case ANIMACY -> Animacy.valueOf(value);
      case MISC -> MiscellaneousFeature.UNSUPPORTED_FEATURE;
    };
  }

  public static Feature of(FeatureType type, String value) {
    return new Feature(type, value);
  }
}