package com.grammr.service.language.morphology;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.event.MorphologicalAnalysisRequest;
import com.grammr.domain.value.language.MorphologicalAnalysis;
import com.grammr.port.rest.MorphologicalAnalysisPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MorphologicalAnalysisService {

  private final MorphologicalAnalysisPort analysisPort;

  public MorphologicalAnalysis analyze(String phrase, LanguageCode languageCode) {
    var analysis = analysisPort.performAnalysis(MorphologicalAnalysisRequest.from(phrase, languageCode));
    log.info("Analysis for phrase '{}' is: {}", phrase, analysis);
    return analysis;
  }
}
