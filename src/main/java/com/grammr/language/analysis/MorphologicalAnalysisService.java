package com.grammr.language.analysis;

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

  public MorphologicalAnalysis analyze(String phrase) {
    var analysis = analysisPort.performAnalysis(phrase);
    log.info("Analysis for phrase '{}' is: {}", phrase, analysis);
    return analysis;
  }
}
