package com.grammr.service.language.translation.semantic;

import com.grammr.domain.value.language.SemanticTranslation;

public interface SemanticTranslationService {

  SemanticTranslation createSemanticTranslation(String phrase);
}
