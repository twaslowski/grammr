package com.grammr.language.service;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.AnalysisComponentRequest;
import com.grammr.domain.value.language.MorphologicalAnalysis;
import com.grammr.language.controller.v1.dto.MorphologicalAnalysisRequest;
import com.grammr.language.port.MorphologicalAnalysisPort;
import com.grammr.language.service.v1.AnalysisComponentProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MorphologyService implements AnalysisComponentProvider {

  private final MorphologicalAnalysisPort analysisPort;

  @Override
  public MorphologicalAnalysis createAnalysisComponent(AnalysisComponentRequest request) {
    return performAnalysis(request.getPhrase(), request.getSourceLanguage());
  }

  public MorphologicalAnalysis performAnalysis(String phrase, LanguageCode sourceLanguage) {
    var analysis = analysisPort.performAnalysis(
        MorphologicalAnalysisRequest.from(phrase, sourceLanguage)
    );
    log.info("Successfully retrieved morphology for phrase '{}'", phrase);
    return analysis;
  }
}
