package com.grammr.chat.controller.v2.dto;

import com.grammr.domain.entity.Chat;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ChatDto(
    UUID chatId,
    ZonedDateTime createdAt,
    ZonedDateTime updatedAt,
    String summary
) {

  public static ChatDto from(Chat chat) {
    return ChatDto.builder()
        .chatId(chat.getChatId())
        .createdAt(chat.getCreatedTimestamp())
        .updatedAt(chat.getUpdatedTimestamp())
        .summary(chat.getSummary())
        .build();
  }
}
