package com.grammr.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.grammr.domain.value.language.MorphologicalAnalysisSpec;
import com.grammr.domain.value.language.Token;
import com.grammr.domain.value.language.TokenTranslation;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class TokenServiceTest {

  private final TokenService tokenService = new TokenService();

  @Test
  void shouldCreateAndPopulateToken() {
    var morphologicalAnalysis = MorphologicalAnalysisSpec.valid().build();
    List<TokenTranslation> translations = List.of(
        new TokenTranslation(1, "hola", "hello", false),
        new TokenTranslation(2, "mundo", "world", false),
        new TokenTranslation(3, "hola", "hello again!", false)
    );

    var tokens = List.of(
        Token.fromString("hola", 1),
        Token.fromString("mundo", 2),
        Token.fromString("hola", 3)
    );

    var enrichedTokens = tokenService.enrichTokens(tokens, translations, morphologicalAnalysis);
    assertThat(enrichedTokens).hasSize(3);

    assertThat(enrichedTokens.getFirst().translation().getTranslation()).isEqualTo("hello");
    assertThat(enrichedTokens.getLast().translation().getTranslation()).isEqualTo("hello again!");
  }

  @Test
  void shouldCreatePlainTextToken() {
    var translations = new ArrayList<TokenTranslation>();
    var analysis = MorphologicalAnalysisSpec.valid().build();

    var tokens = List.of(
        Token.fromString("hello", 1),
        Token.fromString("world",2)
    );

    var enrichedTokens = tokenService.enrichTokens(tokens, translations, analysis);

    assertThat(enrichedTokens).hasSize(2);
    assertThat(enrichedTokens.getFirst().text()).isEqualTo("hello");
    assertThat(enrichedTokens.getFirst().translation()).isNull();
    assertThat(enrichedTokens.getFirst().morphology()).isNull();

    assertThat(enrichedTokens.getLast().text()).isEqualTo("world");
    assertThat(enrichedTokens.getLast().translation()).isNull();
    assertThat(enrichedTokens.getLast().morphology()).isNull();
  }
}