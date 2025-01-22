package com.grammr.domain.event;

import com.grammr.domain.value.language.Token;

public record InflectionRequest(String lemma, String partOfSpeechTag) {

  public static InflectionRequest from(Token token) {
    return new InflectionRequest(token.lemma(), token.partOfSpeechTag().name());
  }
}
