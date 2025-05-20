package com.grammr.service.language.morphology;

import com.grammr.domain.value.AnalysisComponentRequest;
import com.grammr.domain.value.language.MorphologicalAnalysis;
import com.grammr.port.MorphologicalAnalysisPort;
import com.grammr.port.dto.outbound.MorphologicalAnalysisRequest;
import com.grammr.service.language.AnalysisComponentProvider;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MorphologyService implements AnalysisComponentProvider {

  private final MorphologicalAnalysisPort analysisPort;

  @Override
  @Timed("analysis.morphology")
  public MorphologicalAnalysis createAnalysisComponent(AnalysisComponentRequest request) {
    var analysis = analysisPort.performAnalysis(
        MorphologicalAnalysisRequest.from(request.getPhrase(), request.getSourceLanguage())
    );
    log.info("Successfully retrieved morphology for phrase '{}'", request.getPhrase());
    return analysis;
  }
}
