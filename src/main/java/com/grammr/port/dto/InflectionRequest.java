package com.grammr.port.dto;

import com.grammr.domain.value.language.Token;

// todo: tech debt; should be unified with the InflectionsRequest (used external vs this internal one),
// but this will require adding the `Token` datatype to the sidecars
public record InflectionRequest(String lemma, String partOfSpeechTag) {

  public static InflectionRequest from(Token token) {
    return new InflectionRequest(token.lemma(), token.partOfSpeechTag().name());
  }
}
