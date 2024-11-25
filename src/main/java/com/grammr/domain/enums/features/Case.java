package com.grammr.domain.enums.features;

import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Case implements FeatureProperty {

  NOM("Nominative"),
  GEN("Genitive"),
  DAT("Dative"),
  ACC("Accusative"),
  ABL("Ablative"),
  VOC("Vocative"),
  LOC("Locative"),
  INS("Instrumental");

  private final String fullIdentifier;

  public String fullIdentifier() {
    return fullIdentifier;
  }

  @Override
  public FeatureType type() {
    return FeatureType.CASE;
  }

  @Override
  public Set<FeatureCategory> categories() {
    return Set.of(FeatureCategory.NOMINAL);
  }
}
