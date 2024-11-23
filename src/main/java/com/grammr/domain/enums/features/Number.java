package com.grammr.domain.enums.features;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Number {
  SING("Singular"),
  PLUR("Plural");

  private final String fullIdentifier;
}
