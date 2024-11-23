package com.grammr.domain.value;

import com.grammr.domain.value.language.Token;

public class TokenSpec {

  public static Token.TokenBuilder textOnly() {
    return Token.builder()
        .text("text");
  }

}
