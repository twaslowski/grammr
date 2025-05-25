package com.grammr.domain.value;

import com.openai.models.responses.EasyInputMessage;
import com.openai.models.responses.EasyInputMessage.Role;
import java.util.UUID;

public record Message(
    String id,
    String content,
    Role role
) {

  public static Message systemPrompt(String content) {
    return new Message(UUID.randomUUID().toString(), content, Role.SYSTEM);
  }

  public EasyInputMessage toEasyInputMessage() {
    return EasyInputMessage.builder()
        .role(role)
        .content(content)
        .build();
  }
}
