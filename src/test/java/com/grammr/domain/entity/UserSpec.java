package com.grammr.domain.entity;

import com.grammr.domain.entity.User.UserBuilder;
import java.util.UUID;

public class UserSpec {

  public static UserBuilder valid() {
    return User.builder()
        .id(UUID.randomUUID())
        .externalId("some-id");
  }

  public static UserBuilder validWithoutId() {
    return User.builder()
        .externalId("some-id");
  }
}
