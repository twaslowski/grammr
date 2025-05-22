package com.grammr.domain.value;

import com.openai.models.responses.EasyInputMessage;
import com.openai.models.responses.EasyInputMessage.Role;

public record Message(
    String id,
    String content,
    Role role
) {

  public EasyInputMessage convert() {
    return EasyInputMessage.builder()
        .role(role)
        .content(content)
        .build();
  }
}
