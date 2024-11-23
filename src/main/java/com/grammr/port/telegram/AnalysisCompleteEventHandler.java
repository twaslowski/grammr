package com.grammr.port.telegram;

import com.grammr.common.AbstractConsumer;
import com.grammr.common.FullAnalysisStringificationService;
import com.grammr.domain.event.FullAnalysisCompleteEvent;
import com.grammr.port.telegram.dto.response.TelegramResponse;
import com.grammr.port.telegram.dto.response.TelegramTextResponse;
import java.util.concurrent.BlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AnalysisCompleteEventHandler extends AbstractConsumer<FullAnalysisCompleteEvent> {

  private final BlockingQueue<TelegramResponse> outgoingMessageQueue;
  private final FullAnalysisStringificationService fullAnalysisStringificationService;

  public AnalysisCompleteEventHandler(
      BlockingQueue<FullAnalysisCompleteEvent> incomingMessageQueue,
      BlockingQueue<TelegramResponse> outgoingMessageQueue,
      FullAnalysisStringificationService fullAnalysisStringificationService
  ) {
    super(incomingMessageQueue);
    this.outgoingMessageQueue = outgoingMessageQueue;
    this.fullAnalysisStringificationService = fullAnalysisStringificationService;
  }

  @Override
  protected void handleItem(FullAnalysisCompleteEvent fullAnalysisCompleteEvent) {
    var telegramResponse = telegramResponseFrom(fullAnalysisCompleteEvent);
    outgoingMessageQueue.add(telegramResponse);
  }

  private TelegramTextResponse telegramResponseFrom(FullAnalysisCompleteEvent event) {
    return TelegramTextResponse.builder()
        .chatId(event.chatId())
        .text(fullAnalysisStringificationService.stringifyAnalysis(event.fullAnalysis()))
        .build();
  }
}
