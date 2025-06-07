package com.grammr.language.service.translation.literal;

import com.grammr.domain.value.AnalysisComponentRequest;
import com.grammr.domain.value.language.LiteralTranslation;
import com.grammr.language.service.AnalysisComponentProvider;

public interface LiteralTranslationService extends AnalysisComponentProvider {

  LiteralTranslation createAnalysisComponent(AnalysisComponentRequest request);
}

