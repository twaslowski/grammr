package com.grammr.port.telegram.external;

import com.grammr.common.AbstractConsumer;
import com.grammr.port.telegram.dto.response.TelegramResponse;
import com.grammr.port.telegram.dto.response.TelegramTextResponse;
import com.grammr.port.telegram.external.factory.BotApiMessageFactory;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@Slf4j
@Profile("!test")
public class TelegramResponseConsumer extends AbstractConsumer<TelegramResponse> {

  private final TelegramClient telegramClient;

  public TelegramResponseConsumer(
      BlockingQueue<TelegramResponse> outgoingMessageQueue,
      TelegramClient telegramClient
  ) {
    super(outgoingMessageQueue);
    this.telegramClient = telegramClient;
  }

  @Override
  protected void handleItem(TelegramResponse response) {
    var telegramResponseObject = BotApiMessageFactory.createTextResponse((TelegramTextResponse) response);
    try {
      telegramClient.execute(telegramResponseObject);
      log.info("Sent response to chat: {}", telegramResponseObject.getChatId());
    } catch (TelegramApiException | RuntimeException e) {
      log.error("Error while sending message: {}", e.getMessage());
    }
  }
}