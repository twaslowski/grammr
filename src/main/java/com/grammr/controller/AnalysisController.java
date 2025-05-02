package com.grammr.controller;

import com.grammr.domain.event.AnalysisRequest;
import com.grammr.domain.value.FullAnalysis;
import com.grammr.service.AnalysisService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AnalysisController {

  private final AnalysisService analysisService;

  @PostMapping(value = "/analysis", produces = "application/json")
  public ResponseEntity<FullAnalysis> analyze(@RequestBody AnalysisRequest request) {
    log.info("Processing analysis request {}", request);
    var fullAnalysis = analysisService.simpleAnalyze(request);
    return ResponseEntity.ok(fullAnalysis);
  }

  @Timed(value = "rest.translation", description = "Time taken to translate and analyze text")
  @PostMapping(value = "/translation", produces = "application/json")
  public ResponseEntity<FullAnalysis> translateAndAnalyze(@RequestBody AnalysisRequest request) {
    log.info("Processing translation/analysis request {}", request);
    var fullAnalysis = analysisService.translateAndAnalyze(request);
    return ResponseEntity.ok(fullAnalysis);
  }
}
