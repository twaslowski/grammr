package com.grammr.port.telegram;

import com.grammr.common.AbstractConsumer;
import com.grammr.common.AnalysisStringifier;
import com.grammr.domain.event.AnalysisCompleteEvent;
import com.grammr.port.telegram.dto.response.TelegramResponse;
import com.grammr.port.telegram.dto.response.TelegramTextResponse;
import java.util.concurrent.BlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AnalysisCompleteEventConsumer extends AbstractConsumer<AnalysisCompleteEvent> {

  private final BlockingQueue<TelegramResponse> outgoingMessageQueue;

  public AnalysisCompleteEventConsumer(
      BlockingQueue<AnalysisCompleteEvent> incomingMessageQueue,
      BlockingQueue<TelegramResponse> outgoingMessageQueue
  ) {
    super(incomingMessageQueue);
    this.outgoingMessageQueue = outgoingMessageQueue;
  }

  @Override
  protected void handleItem(AnalysisCompleteEvent analysisCompleteEvent) {
    var telegramResponse = telegramResponseFrom(analysisCompleteEvent);
    outgoingMessageQueue.add(telegramResponse);
  }

  private TelegramTextResponse telegramResponseFrom(AnalysisCompleteEvent event) {
    return TelegramTextResponse.builder()
        .chatId(event.chatId())
        .text(AnalysisStringifier.stringifyAnalysis(event.analysis()))
        .build();
  }
}
