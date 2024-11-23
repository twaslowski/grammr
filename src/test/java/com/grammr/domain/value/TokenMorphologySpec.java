package com.grammr.domain.value;

import com.grammr.domain.value.language.TokenMorphology;

public class TokenMorphologySpec {

  public static TokenMorphology.TokenMorphologyBuilder valid() {
    return TokenMorphology.builder()
        .text("text")
        .lemma("lemma");
  }
}
