package com.grammr.chat.value;

import com.grammr.domain.entity.ChatMessage.Role;
import com.openai.models.responses.EasyInputMessage;
import java.util.UUID;
import lombok.Builder;

@Builder
public record Message(
    String id,
    String content,
    Role role,
    String chatId
) {

  public static Message systemPrompt(String content) {
    return new Message(UUID.randomUUID().toString(), content, Role.SYSTEM, null);
  }

  public EasyInputMessage toEasyInputMessage() {
    return EasyInputMessage.builder()
        .role(toEasyInputMessageRole(role))
        .content(content)
        .build();
  }

  public static Message fromAssistant(String response) {
    return Message.builder()
        .content(response)
        .role(Role.ASSISTANT)
        .build();
  }

  private static EasyInputMessage.Role toEasyInputMessageRole(Role role) {
    return switch (role) {
      case SYSTEM -> EasyInputMessage.Role.SYSTEM;
      case USER -> EasyInputMessage.Role.USER;
      case ASSISTANT -> EasyInputMessage.Role.ASSISTANT;
      case DEVELOPER -> EasyInputMessage.Role.DEVELOPER;
    };
  }
}
