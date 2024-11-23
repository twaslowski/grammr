package com.grammr.domain.value;

import com.grammr.domain.value.language.SemanticTranslation;

public class SemanticTranslationSpec {

  public static SemanticTranslation.SemanticTranslationBuilder valid() {
    return SemanticTranslation.builder()
        .sourcePhrase("sourcePhrase")
        .translatedPhrase("translatedPhrase");
  }
}
