package com.grammr.language.translation.semantic;

import com.grammr.domain.value.SemanticTranslation;

public interface SemanticTranslationService {

  SemanticTranslation translate(String phrase);
}
