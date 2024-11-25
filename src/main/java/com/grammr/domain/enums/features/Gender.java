package com.grammr.domain.enums.features;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender implements FeatureProperty {
  MASC("Masculine"),
  FEM("Feminine"),
  NEUT("Neuter");

  private final String fullIdentifier;

  public String fullIdentifier() {
    return fullIdentifier;
  }

  @Override
  public FeatureType type() {
    return FeatureType.GENDER;
  }

  @Override
  public Set<FeatureCategory> categories() {
    return Set.of(FeatureCategory.NOMINAL);
  }
}
