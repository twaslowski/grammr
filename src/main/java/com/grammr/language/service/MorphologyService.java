package com.grammr.language.service;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.MorphologicalAnalysisRequest;
import com.grammr.domain.value.language.MorphologicalAnalysis;
import com.grammr.language.port.MorphologicalAnalysisPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MorphologyService {

  private final MorphologicalAnalysisPort analysisPort;

  public MorphologicalAnalysis createMorphologicalAnalysis(MorphologicalAnalysisRequest request) {
    return performAnalysis(request.getPhrase(), request.getSourceLanguage());
  }

  public MorphologicalAnalysis performAnalysis(String phrase, LanguageCode sourceLanguage) {
    var analysis = analysisPort.performAnalysis(
        com.grammr.language.controller.v1.dto.MorphologicalAnalysisRequest.from(phrase, sourceLanguage)
    );
    log.info("Successfully retrieved morphology for phrase '{}'", phrase);
    return analysis;
  }
}
