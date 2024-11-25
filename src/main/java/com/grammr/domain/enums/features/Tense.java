package com.grammr.domain.enums.features;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Tense implements FeatureProperty {

  PAST("Past"),
  IMP("Imperfect"),
  PRES("Present"),
  PQP("Plusquamperfect"),
  FUT("Future");

  private final String fullIdentifier;

  public String fullIdentifier() {
    return fullIdentifier;
  }

  @Override
  public FeatureType type() {
    return FeatureType.TENSE;
  }

  @Override
  public Set<FeatureCategory> categories() {
    return Set.of(FeatureCategory.NOMINAL);
  }
}
