package com.grammr.domain.enums.features;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Person implements FeatureProperty {

  FIRST("First"),
  SECOND("Second"),
  THIRD("Third");

  private final String fullIdentifier;

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
