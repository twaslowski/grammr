package com.grammr.language.service;

import com.grammr.domain.value.AnalysisComponent;
import com.grammr.domain.value.AnalysisComponentRequest;

public interface AnalysisComponentProvider {

  AnalysisComponent createAnalysisComponent(AnalysisComponentRequest request);
}
