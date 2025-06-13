package com.grammr.language.controller.v1;

import com.grammr.domain.event.AnalysisRequest;
import com.grammr.domain.value.FullAnalysis;
import com.grammr.language.service.v1.AnalysisService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
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

  @PostMapping(value = "/translation", produces = "application/json")
  public ResponseEntity<FullAnalysis> translateAndAnalyze(@RequestBody AnalysisRequest request) {
    log.info("Processing translation/analysis request {}", request);
    var fullAnalysis = analysisService.translateAndAnalyze(request);
    return ResponseEntity.ok(fullAnalysis);
  }
}
