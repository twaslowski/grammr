package com.grammr.language.controller.v2;

import com.grammr.language.controller.v2.dto.PhraseTranslationRequest;
import com.grammr.language.service.v2.AnalysisService;
import com.grammr.language.service.v2.translation.PhraseTranslationService;
import com.grammr.language.service.v2.translation.Translation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("v2TranslationController")
@RequiredArgsConstructor
@RequestMapping("/api/v2/translations")
public class TranslationController {

  private final PhraseTranslationService phraseTranslationService;
  private final AnalysisService analysisService;

//  @Operation(
//      summary = "Translate a phrase",
//      description = "Translates a phrase from the source language to the target language. Optionally performs analysis.",
//      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
//          required = true,
//          content = @Content(schema = @Schema(implementation = PhraseTranslationRequest.class))
//      ),
//      responses = {
//          @ApiResponse(
//              responseCode = "200",
//              description = "Successful translation",
//              content = @Content(schema = @Schema(implementation = Translation.class))
//          )
//      }
//  )
  @PostMapping(value = "/phrase", produces = "application/json")
  public ResponseEntity<Translation> translatePhrase(@RequestBody @Valid PhraseTranslationRequest translationRequest) {
    if (translationRequest.sourceLanguage() == translationRequest.targetLanguage()) {
      throw new IllegalArgumentException("Source and target languages must be different.");
    }
    var translation = phraseTranslationService.translate(translationRequest.phrase(),
        translationRequest.sourceLanguage(),
        translationRequest.targetLanguage());

    if (translationRequest.performAnalysis()) {
      analysisService.simpleAnalyze(translation.translation(), translation.targetLanguage());
    }

    return ResponseEntity.ok(translation);
  }
}
