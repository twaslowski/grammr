package com.grammr.telegram;

import com.grammr.telegram.dto.response.TelegramResponse;
import com.grammr.telegram.dto.update.TelegramUpdate;
import com.grammr.telegram.handler.UpdateHandler;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramUpdateDelegator {

  private final Collection<UpdateHandler> handlers;

  public TelegramResponse delegateUpdate(TelegramUpdate update) {
    var relevantHandler = handlers.stream()
        .filter(handler -> handler.canHandle(update))
        .findFirst();

    return relevantHandler
        .map(handler -> invokeHandler(handler, update))
        .orElseGet(() -> {
          log.warn("No handler found");
          return respondToUnhandleableUpdate(update.getChatId());
        });
  }

  private TelegramResponse invokeHandler(UpdateHandler handler, TelegramUpdate update) {
    try {
      return handler.handleUpdate(update);
    } catch (Exception e) {
      log.error("Error while processing update", e);
      return TelegramResponse.error()
          .chatId(update.getChatId())
          .build();
    }
  }

  private TelegramResponse respondToUnhandleableUpdate(long chatId) {
    return TelegramResponse.unhandleableUpdate()
        .chatId(chatId)
        .build();
  }
}
