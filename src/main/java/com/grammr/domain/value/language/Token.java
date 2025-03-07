package com.grammr.domain.value.language;

import com.grammr.domain.enums.PartOfSpeechTag;
import lombok.Builder;

@Builder
public record Token(
    long index,
    String text,
    TokenTranslation translation,
    TokenMorphology morphology
) {

  public static Token fromString(String text, long index) {
    return new Token(index, text, null, null);
  }

  public Token withTranslation(TokenTranslation tokenTranslation) {
    return new Token(index, text, tokenTranslation, morphology);
  }

  public String lemma() {
    return morphology.lemma();
  }

  public PartOfSpeechTag partOfSpeechTag() {
    return morphology.partOfSpeechTag();
  }

  public Token withMorphology(TokenMorphology morphology) {
    return new Token(index, text, translation, morphology);
  }
}
