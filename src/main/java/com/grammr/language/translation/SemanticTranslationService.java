package com.grammr.language.translation;

import com.grammr.domain.value.SemanticTranslation;

public interface SemanticTranslationService {

  SemanticTranslation translate(String phrase);
}
