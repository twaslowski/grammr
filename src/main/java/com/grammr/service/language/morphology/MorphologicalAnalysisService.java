package com.grammr.service.language.morphology;

import com.grammr.domain.event.MorphologicalAnalysisRequest;
import com.grammr.domain.value.AnalysisComponentRequest;
import com.grammr.domain.value.language.MorphologicalAnalysis;
import com.grammr.port.rest.MorphologicalAnalysisPort;
import com.grammr.service.language.AnalysisComponentProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MorphologicalAnalysisService implements AnalysisComponentProvider {

  private final MorphologicalAnalysisPort analysisPort;

  @Override
  public MorphologicalAnalysis createAnalysisComponent(AnalysisComponentRequest request) {
    var analysis = analysisPort.performAnalysis(
        MorphologicalAnalysisRequest.from(request.getPhrase(), request.getSourceLanguage())
    );
    log.info("Analysis for phrase '{}' is: {}", request.getPhrase(), analysis);
    return analysis;
  }
}
