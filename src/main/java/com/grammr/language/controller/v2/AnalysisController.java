package com.grammr.language.controller.v2;

import com.grammr.domain.value.language.Token;
import com.grammr.language.controller.v2.dto.AnalysisRequest;
import com.grammr.language.controller.v2.dto.PhraseAnalysis;
import com.grammr.language.service.v2.AnalysisService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("v2AnalysisController")
@RequiredArgsConstructor
@RequestMapping("/api/v2/analysis")
public class AnalysisController {

  private final AnalysisService analysisService;

  @PostMapping(produces = "application/json")
  public ResponseEntity<PhraseAnalysis> performAnalysis(@RequestBody @Valid AnalysisRequest analysisRequest) {
    log.info("Processing analysis request: {}", analysisRequest);
    List<Token> analyzedTokens = analysisService.simpleAnalyze(
        analysisRequest.phrase(),
        analysisRequest.languageCode()
    );
    return ResponseEntity.ok(new PhraseAnalysis(
        analysisRequest.phrase(),
        analysisRequest.languageCode(),
        analyzedTokens
    ));
  }
}
