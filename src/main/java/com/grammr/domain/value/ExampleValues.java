package com.grammr.domain.value;

import com.grammr.domain.value.language.TokenTranslation;

public class ExampleValues {

  public static TokenTranslation tokenTranslation() {
    return TokenTranslation.builder()
        .source("Wie")
        .translation("How")
        .build();
  }
}
