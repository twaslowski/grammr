package com.grammr.domain.entity;

import java.util.UUID;

public class UserSessionSpec {

  public static UserSession.UserSessionBuilder valid(User user) {
    return UserSession.builder()
        .user(user)
        .sessionToken(UUID.randomUUID().toString());
  }
}
