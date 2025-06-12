package com.grammr.language.service.v1.translation.literal;

import com.grammr.domain.value.AnalysisComponentRequest;
import com.grammr.domain.value.language.LiteralTranslation;
import com.grammr.language.service.v1.AnalysisComponentProvider;

public interface LiteralTranslationService extends AnalysisComponentProvider {

  LiteralTranslation createAnalysisComponent(AnalysisComponentRequest request);
}

