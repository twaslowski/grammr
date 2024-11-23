package com.grammr.domain.enums.features;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Tense implements FeatureValue {
  PAST("Past"),
  IMP("Imperfect"),
  PRES("Present"),
  PQP("Plusquamperfect"),
  FUT("Future");

  private final String fullIdentifier;
}
