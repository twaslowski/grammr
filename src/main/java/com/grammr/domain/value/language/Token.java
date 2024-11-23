package com.grammr.domain.value.language;

import lombok.Builder;

@Builder
public record Token(
    String text,
    String translation,
    TokenMorphology morphology
) {

  public static Token create(String text) {
    return new Token(text, null, null);
  }

  public Token withTranslation(TokenTranslation tokenTranslation) {
    return new Token(text, tokenTranslation.translation(), morphology);
  }

  public Token withMorphology(TokenMorphology morphology) {
    return new Token(text, translation, morphology);
  }
}
