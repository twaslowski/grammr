package com.grammr.language.service.v1.translation.semantic;

import com.grammr.domain.enums.LanguageCode;

public interface PhraseTranslationService {

  Translation translate(String phrase, LanguageCode sourceLanguage, LanguageCode targetLanguage);
}
