package com.grammr.language.controller.v2;

import com.grammr.domain.value.language.v2.Translation;
import com.grammr.domain.value.language.v2.WordTranslation;
import com.grammr.language.controller.v2.dto.PhraseTranslationRequest;
import com.grammr.language.controller.v2.dto.WordTranslationRequest;
import com.grammr.language.service.v2.analysis.AnalysisService;
import com.grammr.language.service.v2.translation.phrase.PhraseTranslationService;
import com.grammr.language.service.v2.translation.word.WordTranslationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Translation", description = "v2 Translation operations")
@RestController("v2TranslationController")
@RequiredArgsConstructor
@RequestMapping("/api/v2/translations")
public class TranslationController {

  private final PhraseTranslationService phraseTranslationService;
  private final WordTranslationService wordTranslationService;
  private final AnalysisService analysisService;

  @Operation(
      summary = "Translate a phrase",
      description = """
          Translates a phrase from Language A to Language B.
          Optionally performs morphological analysis on the translation.
          """
  )
  @PostMapping(value = "/phrase", produces = "application/json")
  public ResponseEntity<Translation> translatePhrase(@RequestBody @Valid PhraseTranslationRequest translationRequest) {
    if (translationRequest.sourceLanguage() == translationRequest.targetLanguage()) {
      throw new IllegalArgumentException("Source and target languages must be different.");
    }
    var translation = phraseTranslationService.translate(translationRequest.phrase(),
        translationRequest.sourceLanguage(),
        translationRequest.targetLanguage()
    );

    if (translationRequest.performAnalysis()) {
      var analysis = analysisService.performAnalysis(translation.translation(), translation.targetLanguage());
      translation = translation.withTokens(analysis.analysedTokens());
    }

    return ResponseEntity.ok(translation);
  }

  @Operation(
      summary = "Translate a word",
      description = """
          Translates a single word from Language A to Language B within the context
          of a given sentence.
          """
  )
  @PostMapping(value = "/word", produces = "application/json")
  public ResponseEntity<WordTranslation> translateWord(@RequestBody @Valid WordTranslationRequest translationRequest) {
    var translation = wordTranslationService.translate(
        translationRequest.source(),
        translationRequest.context(),
        translationRequest.targetLanguage()
    );

    return ResponseEntity.ok(translation);
  }
}
