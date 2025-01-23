package com.grammr.port.rest;

import com.grammr.domain.event.AnalysisRequestEvent;
import com.grammr.domain.value.FullAnalysis;
import com.grammr.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/v1/analysis")
@RequiredArgsConstructor
public class AnalysisRequestPort {

  private final AnalysisService analysisService;

  @PostMapping("/analyze")
  public ResponseEntity<FullAnalysis> analyze(AnalysisRequestEvent analysisRequestEvent) {
    var analysis = analysisService.processFullAnalysisRequest(analysisRequestEvent);
    return ResponseEntity.ok(analysis);
  }
}
