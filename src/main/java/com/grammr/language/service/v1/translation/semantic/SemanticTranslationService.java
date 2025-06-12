package com.grammr.language.service.v1.translation.semantic;

import com.grammr.domain.value.AnalysisComponentRequest;
import com.grammr.domain.value.language.SemanticTranslation;
import com.grammr.language.service.v1.AnalysisComponentProvider;

public interface SemanticTranslationService extends AnalysisComponentProvider {

  SemanticTranslation createAnalysisComponent(AnalysisComponentRequest request);
}
