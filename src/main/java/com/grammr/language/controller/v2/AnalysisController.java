package com.grammr.language.controller.v2;

import com.grammr.domain.value.language.Token;
import com.grammr.language.controller.v2.dto.AnalysisRequest;
import com.grammr.language.controller.v2.dto.PhraseAnalysis;
import com.grammr.language.service.v2.analysis.AnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Analysis", description = "v2 Analysis operations")
@RestController("v2AnalysisController")
@RequiredArgsConstructor
@RequestMapping("/api/v2/analysis")
public class AnalysisController {

  private final AnalysisService analysisService;

  @Operation(
      summary = "Perform phrase analysis",
      description = "Analyzes a phrase and returns tokenized analysis for the given language."
  )
  @PostMapping(produces = "application/json")
  public ResponseEntity<PhraseAnalysis> performAnalysis(@RequestBody @Valid AnalysisRequest analysisRequest) {
    log.info("Processing analysis request: {}", analysisRequest);
    var phraseAnalysis = analysisService.performAnalysis(
        analysisRequest.phrase(),
        analysisRequest.language()
    );
    return ResponseEntity.ok(phraseAnalysis);
  }

  @GetMapping(value = "/{analysisId}", produces = "application/json")
  public ResponseEntity<PhraseAnalysis> retrieveAnalysis(@PathVariable UUID analysisId) {
    log.info("Retrieving analysis for ID: {}", analysisId);
    var phraseAnalysis = analysisService.retrieveAnalysis(analysisId);
    return ResponseEntity.ok(phraseAnalysis);
  }

  @PutMapping(value = "/{analysisId}", produces = "application/json")
  public ResponseEntity<PhraseAnalysis> updateWithTranslation(
      @PathVariable UUID analysisId,
      @RequestBody @Valid Token token
  ) {
    if (token.translation() == null
        || token.translation().source() == null
        || token.translation().translation() == null
    ) {
      log.warn("Invalid translation data provided for analysis update: {}", token.translation());
      return ResponseEntity.badRequest().build();
    }
    log.info("Updating analysis for ID: {} with token translation '{} -> {}'", analysisId,
        token.translation().source(), token.translation().translation());
    var updatedAnalysis = analysisService.updateAnalysis(analysisId, token);
    return ResponseEntity.ok(updatedAnalysis);
  }
}
