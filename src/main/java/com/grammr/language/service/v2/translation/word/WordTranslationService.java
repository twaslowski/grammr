package com.grammr.language.service.v2.translation.word;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.language.v2.WordTranslation;

public interface WordTranslationService {

  WordTranslation translate(String word, String context, LanguageCode targetLanguage);
}
