package com.grammr.domain.value.language;

public class SemanticTranslationSpec {

  public static SemanticTranslation.SemanticTranslationBuilder valid() {
    return SemanticTranslation.builder()
        .sourcePhrase("sourcePhrase")
        .translatedPhrase("translatedPhrase");
  }
}
