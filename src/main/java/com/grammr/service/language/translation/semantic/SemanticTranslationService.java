package com.grammr.service.language.translation.semantic;

import com.grammr.domain.value.AnalysisComponentRequest;
import com.grammr.domain.value.language.SemanticTranslation;
import com.grammr.service.language.AnalysisComponentProvider;

public interface SemanticTranslationService extends AnalysisComponentProvider {

  SemanticTranslation createAnalysisComponent(AnalysisComponentRequest request);
}
