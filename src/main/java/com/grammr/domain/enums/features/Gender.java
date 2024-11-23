package com.grammr.domain.enums.features;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
  MASC("Masculine"),
  FEM("Feminine"),
  NEUT("Neuter");

  private final String fullIdentifier;
}
