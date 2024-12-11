package com.grammr.telegram;

import com.grammr.common.FullAnalysisStringificationService;
import com.grammr.domain.event.AnalysisCompleteEvent;
import com.grammr.telegram.dto.response.TelegramResponse;
import com.grammr.telegram.dto.response.TelegramTextResponse;
import java.util.concurrent.BlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisCompleteEventHandler {

  private final BlockingQueue<TelegramResponse> outgoingMessageQueue;
  private final FullAnalysisStringificationService fullAnalysisStringificationService;

  @Async
  @EventListener
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
