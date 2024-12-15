package com.grammr.telegram.dto.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class TelegramResponse {

  private long chatId;
  private String text;

  public TelegramResponse(long chatId) {
    this.chatId = chatId;
  }

  public enum ResponseType {
    TEXT,
    INLINE_KEYBOARD
  }

  public abstract ResponseType getResponseType();
}
