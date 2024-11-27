package com.grammr.port.telegram;

import com.grammr.common.AbstractConsumer;
import com.grammr.common.FullAnalysisStringificationService;
import com.grammr.domain.event.AnalysisCompleteEvent;
import com.grammr.port.telegram.dto.response.TelegramResponse;
import com.grammr.port.telegram.dto.response.TelegramTextResponse;
import java.util.concurrent.BlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AnalysisCompleteEventHandler extends AbstractConsumer<AnalysisCompleteEvent> {

  private final BlockingQueue<TelegramResponse> outgoingMessageQueue;
  private final FullAnalysisStringificationService fullAnalysisStringificationService;

  public AnalysisCompleteEventHandler(
      BlockingQueue<AnalysisCompleteEvent> incomingMessageQueue,
      BlockingQueue<TelegramResponse> outgoingMessageQueue,
      FullAnalysisStringificationService fullAnalysisStringificationService
  ) {
    super(incomingMessageQueue);
    this.outgoingMessageQueue = outgoingMessageQueue;
    this.fullAnalysisStringificationService = fullAnalysisStringificationService;
  }

  @Override
  protected void handleItem(AnalysisCompleteEvent analysisCompleteEvent) {
    var telegramResponse = telegramResponseFrom(analysisCompleteEvent);
    outgoingMessageQueue.add(telegramResponse);
  }

  private TelegramTextResponse telegramResponseFrom(AnalysisCompleteEvent event) {
    return TelegramTextResponse.builder()
        .chatId(event.chatId())
        .text(fullAnalysisStringificationService.stringifyAnalysis(event.fullAnalysis()))
        .build();
  }
}
