package com.grammr.language.analysis;

import com.grammr.domain.value.language.GrammaticalAnalysis;
import com.grammr.port.rest.GrammaticalAnalysisPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GrammaticalAnalysisService {

  private final GrammaticalAnalysisPort analysisPort;

  public GrammaticalAnalysis analyze(String phrase) {
    var analysis = analysisPort.performAnalysis(phrase);
    log.info("Analysis for phrase '{}' is: {}", phrase, analysis);
    return analysis;
  }
}
