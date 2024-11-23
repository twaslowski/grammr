package com.grammr.domain.enums.features;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Case {

  NOM("Nominative"),
  GEN("Genitive"),
  DAT("Dative"),
  ACC("Accusative"),
  ABL("Ablative"),
  VOC("Vocative"),
  LOC("Locative"),
  INS("Instrumental");

  private final String fullIdentifier;
}
