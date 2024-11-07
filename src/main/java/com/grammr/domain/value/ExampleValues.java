package com.grammr.domain.value;

import com.grammr.domain.value.language.TranslatedToken;

public class ExampleValues {

  public static TranslatedToken translatedToken() {
    return TranslatedToken.builder()
        .source("Wie")
        .translation("How")
        .build();
  }
}
