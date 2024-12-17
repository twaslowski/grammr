package com.grammr.service.language.translation.literal;

import com.grammr.domain.value.AnalysisComponentRequest;
import com.grammr.domain.value.language.LiteralTranslation;
import com.grammr.service.language.AnalysisComponentProvider;

public interface LiteralTranslationService extends AnalysisComponentProvider {

  LiteralTranslation createAnalysisComponent(AnalysisComponentRequest request);
}

