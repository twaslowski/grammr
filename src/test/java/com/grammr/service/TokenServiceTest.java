package com.grammr.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.grammr.domain.value.language.MorphologicalAnalysisSpec;
import com.grammr.domain.value.language.Token;
import com.grammr.language.service.v1.TokenService;
import java.util.List;
import org.junit.jupiter.api.Test;

class TokenServiceTest {

  private final TokenService tokenService = new TokenService();

  @Test
  void shouldExtractWhitespaceSeparatedTokens() {
    String sentence = "The quick brown fox jumps over the lazy dog";
    var tokens = tokenService.tokenize(sentence);
    assertEquals(9, tokens.size());
  }

  @Test
  void shouldDisregardArbitraryWhitespaces() {
    String sentence = "The quick brown    fox jumps over the lazy dog";
    var tokens = tokenService.tokenize(sentence);
    assertEquals(9, tokens.size());
  }

  @Test
  void shouldTokenizePhrase() {
    var phrase = "Hello, world!";
    var tokens = tokenService.tokenize(phrase);

    assertThat(tokens).hasSize(4);
    assertThat(tokens.get(0).text()).isEqualTo("Hello");
    assertThat(tokens.get(1).text()).isEqualTo(",");
    assertThat(tokens.get(2).text()).isEqualTo("world");
    assertThat(tokens.get(3).text()).isEqualTo("!");
  }

  @Test
  void shouldCreateAndPopulateToken() {
    var morphologicalAnalysis = MorphologicalAnalysisSpec.valid().build();

    var tokens = List.of(
        Token.fromString("hola", 1),
        Token.fromString("mundo", 2),
        Token.fromString("hola", 3)
    );

    var enrichedTokens = tokenService.enrichTokens(tokens, morphologicalAnalysis);
    assertThat(enrichedTokens).hasSize(3);
  }

  @Test
  void shouldCreatePlainTextToken() {
    var analysis = MorphologicalAnalysisSpec.valid().build();

    var tokens = List.of(
        Token.fromString("hello", 1),
        Token.fromString("world", 2)
    );

    var enrichedTokens = tokenService.enrichTokens(tokens, analysis);

    assertThat(enrichedTokens).hasSize(2);
    assertThat(enrichedTokens.getFirst().text()).isEqualTo("hello");
    assertThat(enrichedTokens.getFirst().translation()).isNull();
    assertThat(enrichedTokens.getFirst().morphology()).isNull();

    assertThat(enrichedTokens.getLast().text()).isEqualTo("world");
    assertThat(enrichedTokens.getLast().translation()).isNull();
    assertThat(enrichedTokens.getLast().morphology()).isNull();
  }
}