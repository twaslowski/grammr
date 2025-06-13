package com.grammr.language.controller.v1;

import com.grammr.domain.value.language.TokenTranslation;
import com.grammr.language.controller.v1.dto.LiteralTranslationRequestDto;
import com.grammr.language.service.v1.translation.literal.ContextFreeWordTranslationService;
import com.grammr.language.service.v1.translation.literal.OpenAITokenTranslationService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/translate")
public class WordTranslationController {

  private final OpenAITokenTranslationService tokenTranslationService;
  private final ContextFreeWordTranslationService wordTranslationService;

  @PostMapping("/word/context")
  public ResponseEntity<TokenTranslation> translateWithContext(@RequestBody @Valid LiteralTranslationRequestDto request) {
    var tokenTranslation = tokenTranslationService.createTranslationForSingleWord(
        request.phrase(), request.word(), request.targetLanguage()
    );
    return ResponseEntity.ok(tokenTranslation);
  }

  @PostMapping("/word/no-context")
  public ResponseEntity<?> translateAll(@RequestBody LiteralTranslationRequestDto request) {
    var result = wordTranslationService.create(request.word(), request.targetLanguage(), request.sourceLanguage());
    return ResponseEntity.ok(result);
  }
}
