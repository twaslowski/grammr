package com.grammr.language.analysis;

import com.grammr.domain.value.language.GrammaticalAnalysis;
import com.grammr.port.rest.GrammaticalAnalysisPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GrammaticalAnalysisService {

  private final GrammaticalAnalysisPort analysisPort;

  public GrammaticalAnalysis analyze(String phrase) {
    return analysisPort.performAnalysis(phrase);
  }
}
