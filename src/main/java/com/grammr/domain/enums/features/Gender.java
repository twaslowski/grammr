package com.grammr.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Gender {

  MASC("masculine"),
  FEM("feminine"),
  NEUT("neuter");

  private final String name;
}
