package com.grammr.language.service.v2.translation.phrase;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.language.v2.Translation;

public interface PhraseTranslationService {

  Translation translate(String phrase, LanguageCode sourceLanguage, LanguageCode targetLanguage);
}
