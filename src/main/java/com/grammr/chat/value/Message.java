package com.grammr.chat.value;

import com.grammr.domain.entity.ChatMessage;
import com.grammr.domain.entity.ChatMessage.Role;
import com.openai.models.responses.EasyInputMessage;
import java.util.UUID;
import lombok.Builder;

@Builder
public record Message(
    UUID id,
    String content,
    Role role,
    String chatId,
    UUID analysisId
) {

  public static Message systemPrompt(String content) {
    return new Message(UUID.randomUUID(), content, Role.SYSTEM, null, null);
  }

  public static Message userMessage(String content) {
    return new Message(UUID.randomUUID(), content, Role.USER, null, null);
  }

  public EasyInputMessage toEasyInputMessage() {
    return EasyInputMessage.builder()
        .role(toEasyInputMessageRole(role))
        .content(content)
        .build();
  }

  public static Message fromChatMessage(ChatMessage chatMessage) {
    return Message.builder()
        .id(chatMessage.getMessageId())
        .content(chatMessage.getContent())
        .role(chatMessage.getRole())
        .chatId(chatMessage.getChat().getChatId().toString())
        .analysisId(chatMessage.getAnalysisId())
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
