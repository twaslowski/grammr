package com.grammr.telegram;

import com.grammr.common.AbstractConsumer;
import com.grammr.telegram.config.LogContext;
import com.grammr.telegram.dto.response.TelegramResponse;
import com.grammr.telegram.dto.update.TelegramUpdate;
import java.util.concurrent.BlockingQueue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TelegramUpdateConsumer extends AbstractConsumer<TelegramUpdate> {

  private final TelegramUpdateDelegator telegramUpdateDelegator;
  private final BlockingQueue<TelegramResponse> outgoingMessageQueue;

  public TelegramUpdateConsumer(BlockingQueue<TelegramUpdate> incomingMessageQueue,
                                TelegramUpdateDelegator telegramUpdateDelegator,
                                BlockingQueue<TelegramResponse> outgoingMessageQueue) {
    super(incomingMessageQueue);
    this.outgoingMessageQueue = outgoingMessageQueue;
    this.telegramUpdateDelegator = telegramUpdateDelegator;
  }

  @Override
  protected void handleItem(TelegramUpdate update) {
    LogContext.enrichWithUpdate(update);

    var response = telegramUpdateDelegator.delegateUpdate(update);
    log.info("Received response for update. Queueing for sending.");

    LogContext.clear();
    outgoingMessageQueue.add(response);
  }
}
