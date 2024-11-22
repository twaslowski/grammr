package com.grammr.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.grammr.service.TokenService;
import org.junit.jupiter.api.Test;

class TokenizerTest {

  private final TokenService tokenizer = new TokenService();

  @Test
  void shouldExtractWhitespaceSeparatedTokens() {
    String sentence = "The quick brown fox jumps over the lazy dog";
    var tokens = tokenizer.tokenize(sentence);
    assertEquals(9, tokens.size());
  }

  @Test
  void shouldDisregardCommas() {
    String sentence = "The quick, brown . fox jumps over,.- the lazy dog!";
    var tokens = tokenizer.tokenize(sentence);
    assertEquals(9, tokens.size());
  }

  @Test
  void shouldDisregardArbitraryWhitespaces() {
    String sentence = "The quick brown    fox jumps over the lazy dog";
    var tokens = tokenizer.tokenize(sentence);
    assertEquals(9, tokens.size());
  }
}