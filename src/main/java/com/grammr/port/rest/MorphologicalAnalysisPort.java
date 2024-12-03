package com.grammr.port.rest;

import com.grammr.domain.event.MorphologicalAnalysisRequest;
import com.grammr.domain.value.language.MorphologicalAnalysis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Slf4j
@Service
public class MorphologicalAnalysisPort {

  private final RestClient restClient;

  public MorphologicalAnalysis performAnalysis(MorphologicalAnalysisRequest analysisRequest) {
    try {
      return restClient
          .post()
          .uri("/morphological-analysis")
          .body(analysisRequest)
          .retrieve()
          .body(MorphologicalAnalysis.class);
    } catch (Exception e) {
      log.error("Error performing analysis for phrase: {}", analysisRequest.phrase(), e);
      return null;
    }
  }
}
