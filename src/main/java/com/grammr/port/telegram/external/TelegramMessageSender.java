package com.grammr.port.telegram.external;

import com.grammr.port.telegram.dto.response.TelegramResponse;
import com.grammr.port.telegram.dto.response.TelegramTextResponse;
import com.grammr.port.telegram.external.factory.BotApiMessageFactory;
import jakarta.annotation.PostConstruct;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class TelegramMessageSender {

  private final Queue<TelegramResponse> outgoingMessageQueue;
  private final TelegramClient telegramClient;

  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  @PostConstruct
  public void init() {
    log.info("Starting outgoing queue processor ...");
    scheduler.scheduleWithFixedDelay(this::sendResponses, 0, 50, TimeUnit.MILLISECONDS);
  }

  public void sendResponses() {
    if (!outgoingMessageQueue.isEmpty()) {
      var response = outgoingMessageQueue.remove();
      var telegramResponseObject = BotApiMessageFactory.createTextResponse((TelegramTextResponse) response);
      try {
        telegramClient.execute(telegramResponseObject);
        log.info("Sent response to chat: {}", response.getChatId());
      } catch (TelegramApiException | RuntimeException e) {
        log.error("Error while sending message: {}", e.getMessage());
      }
    }
  }
}