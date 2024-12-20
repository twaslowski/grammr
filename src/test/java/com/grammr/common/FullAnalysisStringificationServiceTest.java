package com.grammr.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.value.language.SemanticTranslationSpec;
import com.grammr.domain.value.language.TokenMorphologySpec;
import com.grammr.domain.value.language.TokenSpec;
import java.util.List;
import com.grammr.domain.value.language.TokenTranslation;
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
    var tokens = List.of(TokenSpec.textOnly().build());
    var result = fullAnalysisStringificationService.stringifyTokens(tokens);
    assertEquals("", result);
  }

  @Test
  void shouldStringifyTokensWithTranslation() {
    var tokens = List.of(TokenSpec.textOnly()
        .translation(new TokenTranslation("text", "translation"))
        .build());
    var result = fullAnalysisStringificationService.stringifyTokens(tokens);
    assertEquals("<b>text</b> -> translation", result);
  }

  @Test
  void shouldStringifyTokensWithMorphology() {
    var morphology = TokenMorphologySpec.valid().build();
    var tokens = List.of(TokenSpec.textOnly()
        .morphology(morphology)
        .build());
    var result = fullAnalysisStringificationService.stringifyTokens(tokens);
    assertEquals(", (from <i>lemma</i>), Noun, nominative singular masculine", result);
  }

  @Test
  void shouldNotStringifyMorphologyIfTokenIsNotInflected() {
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

  @Test
  void shouldNotStringifyFeaturesIfCategoryIsNeitherNominalNorVerbal() {
    var morphology = TokenMorphologySpec.valid()
        .text("someText")
        .lemma("someOtherText")
        .partOfSpeechTag(PartOfSpeechTag.CCONJ)
        .build();

    var tokens = List.of(TokenSpec.textOnly()
        .morphology(morphology)
        .build());
    var result = fullAnalysisStringificationService.stringifyTokens(tokens);
    assertEquals(", (from <i>someOtherText</i>), Coordinating Conjunction", result);
  }
}