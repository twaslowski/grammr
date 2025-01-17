package com.grammr.telegram.config;

import com.grammr.telegram.dto.update.TelegramUpdate;
import org.slf4j.MDC;

public class LogContext {

  public static void enrichWithUpdate(TelegramUpdate update) {
    MDC.put("chatId", String.valueOf(update.getChatId()));
  }

  public static void clear() {
    MDC.clear();
  }
}
