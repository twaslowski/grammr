package com.grammr.telegram;

import com.grammr.service.UserService;
import com.grammr.telegram.dto.response.TelegramResponse;
import com.grammr.telegram.dto.update.TelegramUpdate;
import com.grammr.telegram.exception.UserNotFoundException;
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
  private final TelegramErrorHandler telegramErrorHandler;
  private final UserService userService;

  public TelegramResponse delegateUpdate(TelegramUpdate update) {
    var relevantHandler = handlers.stream()
        .filter(handler -> handler.canHandle(update))
        .findFirst();

    return relevantHandler
        .map(handler -> invokeHandler(handler, update))
        .orElseGet(() -> {
          log.warn("No handler found for update {}", update);
          return telegramErrorHandler.noHandlerFound(update.getChatId());
        });
  }

  private TelegramResponse invokeHandler(UpdateHandler handler, TelegramUpdate update) {
    try {
      enrichUpdateWithUser(update);
      return handler.handleUpdate(update);
    } catch (UserNotFoundException e) {
      log.error("User not found", e);
      return telegramErrorHandler.userUnknown(update.getChatId());
    } catch (Exception e) {
      log.error("Error while processing update", e);
      return telegramErrorHandler.error(update.getChatId());
    }
  }

  private void enrichUpdateWithUser(TelegramUpdate update) {
    var user = userService.findUserByChatId(update.getChatId()).orElseGet(
        () -> userService.createUserFromChatId(update.getChatId()));
    update.setUser(user);
  }
}
