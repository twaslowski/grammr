package com.grammr.language.service.v2.translation.phrase;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.language.service.v2.translation.Translation;

public interface PhraseTranslationService {

  Translation translate(String phrase, LanguageCode sourceLanguage, LanguageCode targetLanguage);
}
