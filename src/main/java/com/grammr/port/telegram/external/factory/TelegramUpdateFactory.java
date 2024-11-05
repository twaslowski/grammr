package com.grammr.port.telegram.external.factory;

import com.grammr.port.telegram.dto.update.TelegramTextUpdate;
import com.grammr.port.telegram.dto.update.TelegramUpdate;
import java.util.Optional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class TelegramUpdateFactory {

  public static TelegramUpdate createTelegramUpdate(Update update) {
    return TelegramTextUpdate.builder()
        .updateId(update.getUpdateId())
        .text(extractText(update))
        .chatId(extractChatId(update))
        .build();
  }

  private static String extractText(Update update) {
    var message = Optional.ofNullable(update.getMessage());
    return message.map(Message::getText).orElse("");
  }

  private static long extractChatId(Update update) {
    if (update.hasCallbackQuery()) {
      return update.getCallbackQuery().getMessage().getChatId();
    } else {
      return update.getMessage().getChatId();
    }
  }
}
