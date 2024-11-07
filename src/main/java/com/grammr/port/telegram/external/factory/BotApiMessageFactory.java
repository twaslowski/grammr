package com.grammr.port.telegram.external.factory;

import com.grammr.port.telegram.dto.response.TelegramTextResponse;
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