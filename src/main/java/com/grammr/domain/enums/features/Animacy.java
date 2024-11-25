package com.grammr.domain.enums.features;

import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Animacy implements FeatureProperty {

  ANIM("Animate"),
  INAN("Inanimate");

  private final String fullIdentifier;

  @Override
  public String fullIdentifier() {
    return fullIdentifier;
  }

  @Override
  public FeatureType type() {
    return FeatureType.ANIMACY;
  }

  @Override
  public Set<FeatureCategory> categories() {
    return Set.of(FeatureCategory.NOMINAL);
  }
}
