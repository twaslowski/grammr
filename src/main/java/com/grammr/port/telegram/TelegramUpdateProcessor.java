package com.grammr.port.telegram;

import com.grammr.port.telegram.config.LogContext;
import com.grammr.port.telegram.dto.response.TelegramResponse;
import com.grammr.port.telegram.dto.update.TelegramUpdate;
import jakarta.annotation.PostConstruct;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramUpdateProcessor {

  private final Queue<TelegramUpdate> incomingMessageQueue;
  private final Queue<TelegramResponse> outgoingMessageQueue;
  private final TelegramUpdateDelegator telegramUpdateDelegator;

  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  @PostConstruct
  public void init() {
    log.info("Starting incoming queue processor ...");
    scheduler.scheduleWithFixedDelay(this::processUpdate, 0, 50, TimeUnit.MILLISECONDS);
  }

  public void processUpdate() {
    if (!incomingMessageQueue.isEmpty()) {
      var update = incomingMessageQueue.remove();
      LogContext.enrichWithUpdate(update);

      var response = telegramUpdateDelegator.delegateUpdate(update);
      log.info("Received response for update. Queueing for sending.");

      LogContext.clear();
      outgoingMessageQueue.add(response);
    }
  }
}
