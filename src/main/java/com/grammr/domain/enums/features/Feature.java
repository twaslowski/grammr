package com.grammr.domain.enums.features;

public record Feature(FeatureType type, String value) {

  // todo:  Include validation at deserialization time instead of retrieval.
  public Enum<?> getEnumValue() {
    return switch (type) {
      case GENDER -> Gender.valueOf(value);
      case CASE -> Case.valueOf(value);
      default -> throw new IllegalArgumentException("Unknown FeatureType: " + type);
    };
  }
}