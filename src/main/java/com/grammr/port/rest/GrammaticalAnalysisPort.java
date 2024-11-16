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

  public GrammaticalAnalysis performAnalysis(GrammaticalAnalysisRequest request) {
    return null;
  }
}
