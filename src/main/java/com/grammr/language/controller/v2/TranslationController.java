package com.grammr.language.controller.v2;

import com.grammr.domain.value.language.v2.Translation;
import com.grammr.domain.value.language.v2.WordTranslation;
import com.grammr.language.controller.v2.dto.PhraseTranslationRequest;
import com.grammr.language.controller.v2.dto.WordTranslationRequest;
import com.grammr.language.service.v2.AnalysisService;
import com.grammr.language.service.v2.translation.phrase.PhraseTranslationService;
import com.grammr.language.service.v2.translation.word.WordTranslationService;
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
  private final WordTranslationService wordTranslationService;
  private final AnalysisService analysisService;

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
      var analysis = analysisService.simpleAnalyze(translation.translation(), translation.targetLanguage());
      translation = translation.withTokens(analysis);
    }

    return ResponseEntity.ok(translation);
  }

  @PostMapping(value = "/word", produces = "application/json")
  public ResponseEntity<WordTranslation> translateWord(@RequestBody @Valid WordTranslationRequest translationRequest) {
    var translation = wordTranslationService.translate(
        translationRequest.source(),
        translationRequest.context(),
        translationRequest.targetLanguage()
    ).withSourceLanguage(translationRequest.sourceLanguage());

    return ResponseEntity.ok(translation);
  }
}
