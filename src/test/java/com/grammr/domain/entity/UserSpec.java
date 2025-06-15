package com.grammr.domain.entity;

import com.grammr.domain.entity.User.UserBuilder;
import java.util.UUID;

public class UserSpec {

  public static UserBuilder valid() {
    return User.builder()
        .id(UUID.randomUUID())
        .deleted(false)
        .externalId("some-id");
  }

  public static UserBuilder validWithoutId() {
    return User.builder()
        .deleted(false)
        .externalId("some-id");
  }
}
