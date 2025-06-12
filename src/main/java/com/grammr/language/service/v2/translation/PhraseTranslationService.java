package com.grammr.language.service.v2.translation;

import com.grammr.domain.enums.LanguageCode;

public interface PhraseTranslationService {

  Translation translate(String phrase, LanguageCode sourceLanguage, LanguageCode targetLanguage);
}
