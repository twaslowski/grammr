package com.grammr.domain.enums.features;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Person implements FeatureProperty {

  FIRST("First person"),
  SECOND("Second person"),
  THIRD("Third person");

  private final String fullIdentifier;

  public static Person from(String value) {
    return switch (value) {
      case "1", "FIRST" -> FIRST;
      case "2", "SECOND" -> SECOND;
      case "3", "THIRD" -> THIRD;
      default -> throw new IllegalArgumentException("Unsupported person value: " + value);
    };
  }

  public String fullIdentifier() {
    return fullIdentifier;
  }

  @Override
  public FeatureType type() {
    return FeatureType.PERSON;
  }

  @Override
  public Set<FeatureCategory> categories() {
    return Set.of(FeatureCategory.NOMINAL);
  }
}
