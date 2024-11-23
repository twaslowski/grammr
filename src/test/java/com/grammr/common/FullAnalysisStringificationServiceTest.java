package com.grammr.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.grammr.domain.value.SemanticTranslationSpec;
import com.grammr.domain.value.TokenMorphologySpec;
import com.grammr.domain.value.TokenSpec;
import java.util.List;
import org.junit.jupiter.api.Test;

class FullAnalysisStringificationServiceTest {

  private final FullAnalysisStringificationService fullAnalysisStringificationService = new FullAnalysisStringificationService();

  @Test
  void shouldStringifySemanticTranslation() {
    var semanticTranslation = SemanticTranslationSpec.valid().build();
    var result = fullAnalysisStringificationService.stringifySemanticTranslation(semanticTranslation);
    assertEquals("<b>sourcePhrase</b> translates to <b>translatedPhrase</b>", result);
  }

  @Test
  void shouldStringifyTokens() {
    // todo: this is a scenario that should not actually occur; perhaps, a stringified token with
    // nothing else should be null
    var tokens = List.of(TokenSpec.textOnly().build());
    var result = fullAnalysisStringificationService.stringifyTokens(tokens);
    assertEquals("", result);
  }

  @Test
  void shouldStringifyTokensWithTranslation() {
    var tokens = List.of(TokenSpec.textOnly().translation("translation").build());
    var result = fullAnalysisStringificationService.stringifyTokens(tokens);
    assertEquals("<b>text</b> -> <b>translation</b>", result);
  }

  @Test
  void shouldStringifyTokensWithMorphology() {
    var morphology = TokenMorphologySpec.valid().build();
    var tokens = List.of(TokenSpec.textOnly()
        .morphology(morphology)
        .build());
    var result = fullAnalysisStringificationService.stringifyTokens(tokens);
    assertEquals("(from <i>lemma</i>)", result);
  }

  @Test
  void shouldNotStringifyMorphologyIfTokenTextEqualsLemma() {
    var morphology = TokenMorphologySpec.valid()
        .text("someText")
        .lemma("someText")
        .build();
    var tokens = List.of(TokenSpec.textOnly()
        .morphology(morphology)
        .build());
    var result = fullAnalysisStringificationService.stringifyTokens(tokens);
    assertEquals("", result);
  }
}