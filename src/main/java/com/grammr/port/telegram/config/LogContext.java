package com.grammr.port.telegram.config;

import com.grammr.port.telegram.dto.update.TelegramUpdate;
import org.slf4j.MDC;

public class LogContext {

  public static void enrichWithUpdate(TelegramUpdate update) {
    MDC.put("updateId", String.valueOf(update.getUpdateId()));
    MDC.put("chatId", String.valueOf(update.getChatId()));
  }

  public static void clear() {
    MDC.clear();
  }
}
