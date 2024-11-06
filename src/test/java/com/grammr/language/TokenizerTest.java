package com.grammr.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TokenizerTest {

  private final Tokenizer tokenizer = new Tokenizer();

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