package com.grammr.domain.enums.features;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Number implements FeatureProperty {
  SING("Singular"),
  PLUR("Plural");

  private final String fullIdentifier;

  public String fullIdentifier() {
    return fullIdentifier;
  }

  @Override
  public FeatureType type() {
    return FeatureType.NUMBER;
  }

  @Override
  public Set<FeatureCategory> categories() {
    return Set.of(FeatureCategory.NOMINAL, FeatureCategory.VERBAL);
  }
}
