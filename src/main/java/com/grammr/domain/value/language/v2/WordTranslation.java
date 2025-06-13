package com.grammr.domain.value.language.v2;

import com.grammr.domain.enums.LanguageCode;
import lombok.Builder;

@Builder
public record WordTranslation(
    String source,
    String translation,
    LanguageCode sourceLanguage,
    LanguageCode targetLanguage,
    String context
) {

  public WordTranslation withSourceLanguage(LanguageCode sourceLanguage) {
    return new WordTranslation(
        source,
        translation,
        sourceLanguage,
        targetLanguage,
        context
    );
  }
}
