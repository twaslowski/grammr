package com.grammr.port.rest;

import com.grammr.domain.event.GrammaticalAnalysisRequest;
import com.grammr.domain.value.language.GrammaticalAnalysis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Slf4j
@Service
public class GrammaticalAnalysisPort {

  private final RestClient restClient;

  public GrammaticalAnalysis performAnalysis(String phrase) {
    try {
      return restClient
          .post()
          .uri("/grammatical-analysis")
          .body(GrammaticalAnalysisRequest.from(phrase))
          .retrieve()
          .body(GrammaticalAnalysis.class);
    } catch (Exception e) {
      log.error("Error performing analysis for phrase: {}", phrase, e);
      return null;
    }
  }
}
