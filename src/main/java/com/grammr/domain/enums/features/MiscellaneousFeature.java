package com.grammr.domain.enums.features;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MiscellaneousFeature implements FeatureProperty {
  UNSUPPORTED_FEATURE("Unsupported Morphological Feature.");

  private final String value;

  @Override
  public String fullIdentifier() {
    return value;
  }

  @Override
  public FeatureType type() {
    return FeatureType.MISC;
  }

  @Override
  public Set<FeatureCategory> categories() {
    return Set.of();
  }
}
