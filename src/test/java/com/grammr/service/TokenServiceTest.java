package com.grammr.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.grammr.domain.value.LiteralTranslationSpec;
import com.grammr.domain.value.MorphologicalAnalysisSpec;
import com.grammr.domain.value.language.TokenTranslation;
import java.util.List;
import org.junit.jupiter.api.Test;

class TokenServiceTest {

  private final TokenService tokenService = new TokenService();

  @Test
  void shouldCreateAndPopulateToken() {
    var literalTranslation = LiteralTranslationSpec.valid()
        .tokenTranslations(List.of(new TokenTranslation("hola", "hello")))
        .build();
    var morphologicalAnalysis = MorphologicalAnalysisSpec.valid().build();

    var tokens = List.of("hola", "world");

    var enrichedTokens = tokenService.consolidateTokens(tokens, morphologicalAnalysis, literalTranslation);
    assertThat(enrichedTokens).hasSize(2);

    assertThat(enrichedTokens.getFirst().translation()).isEqualTo("hello");
  }

  @Test
  void shouldCreatePlainTextToken() {
    var literalTranslation = LiteralTranslationSpec.valid().build();
    var analysis = MorphologicalAnalysisSpec.valid().build();

    var tokens = List.of("hello", "world");

    var enrichedTokens = tokenService.consolidateTokens(tokens, analysis, literalTranslation);

    assertThat(enrichedTokens).hasSize(2);
    assertThat(enrichedTokens.getFirst().text()).isEqualTo("hello");
    assertThat(enrichedTokens.getFirst().translation()).isNull();
    assertThat(enrichedTokens.getFirst().morphology()).isNull();

    assertThat(enrichedTokens.getLast().text()).isEqualTo("world");
    assertThat(enrichedTokens.getLast().translation()).isNull();
    assertThat(enrichedTokens.getLast().morphology()).isNull();
  }
}