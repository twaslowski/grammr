package com.grammr.domain.value.language;

public class TokenSpec {

  public static Token.TokenBuilder textOnly() {
    return Token.builder()
        .text("text");
  }

}
