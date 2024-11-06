package com.grammr.domain.value;

public class ExampleValues {

  public static TranslatedToken translatedToken() {
    return TranslatedToken.builder()
        .source("Wie")
        .translation("How")
        .build();
  }
}
