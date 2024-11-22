package com.grammr.domain.value;

import com.grammr.domain.value.language.LiteralTranslation;
import java.util.List;

public class LiteralTranslationSpec {

  public static LiteralTranslation.LiteralTranslationBuilder valid() {
    return LiteralTranslation.builder()
        .sourcePhrase("some phrase")
        .tokenTranslations(List.of());
  }
}
