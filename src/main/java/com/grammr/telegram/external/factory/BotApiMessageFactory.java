package com.grammr.telegram.external.factory;

import com.grammr.telegram.dto.response.TelegramTextResponse;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class BotApiMessageFactory {

  public static SendMessage createTextResponse(TelegramTextResponse response) {
    return SendMessage.builder()
        .chatId(response.getChatId())
        .parseMode("HTML")
        .text(response.getText())
        .build();
  }
}