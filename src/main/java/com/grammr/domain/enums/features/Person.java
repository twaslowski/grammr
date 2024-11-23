package com.grammr.domain.enums.features;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Person implements FeatureValue {
  FIRST("First"),
  SECOND("Second"),
  THIRD("Third");
  private final String fullIdentifier;
}
