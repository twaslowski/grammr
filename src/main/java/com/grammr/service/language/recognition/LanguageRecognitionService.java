package com.grammr.service.language.recognition;

import com.grammr.domain.value.AnalysisComponentRequest;
import com.grammr.domain.value.language.LanguageRecognition;
import com.grammr.service.language.AnalysisComponentProvider;

public interface LanguageRecognitionService extends AnalysisComponentProvider {

  LanguageRecognition createAnalysisComponent(AnalysisComponentRequest request);
}
