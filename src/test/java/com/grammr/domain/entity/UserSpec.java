package com.grammr.domain.entity;

import com.grammr.domain.entity.User.UserBuilder;
import com.grammr.domain.enums.LanguageCode;
import java.util.UUID;

public class UserSpec {

  public static UserBuilder valid() {
    return User.builder()
        .email("someuser@email.com")
        .password("password-hash");
  }
}
