package com.grammr.language.controller.v2;

import com.grammr.language.controller.v2.dto.PhraseTranslationRequest;
import com.grammr.language.service.v1.translation.semantic.PhraseTranslationService;
import com.grammr.language.service.v1.translation.semantic.Translation;
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

  public final PhraseTranslationService phraseTranslationService;

  @PostMapping(value = "/phrase", produces = "application/json")
  public ResponseEntity<Translation> translatePhrase(@RequestBody @Valid PhraseTranslationRequest translationRequest) {
    var translation = phraseTranslationService.translate(translationRequest.phrase(),
        translationRequest.sourceLanguage(),
        translationRequest.targetLanguage());
    return ResponseEntity.ok(translation);
  }
}
