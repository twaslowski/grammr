package com.grammr.domain.value.language;

import java.util.List;

public class LiteralTranslationSpec {

  public static LiteralTranslation.LiteralTranslationBuilder valid() {
    return LiteralTranslation.builder()
        .sourcePhrase("some phrase")
        .tokenTranslations(List.of());
  }
}
