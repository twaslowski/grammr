package com.grammr.service;

import com.grammr.domain.event.AnalysisCompleteEvent;
import com.grammr.domain.event.AnalysisRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisRequestHandler {

  private final FullAnalysisService fullAnalysisService;
  private final ApplicationEventPublisher eventPublisher;

  @Async
  @EventListener
  protected void handleItem(AnalysisRequestEvent analysisRequest) {
    log.info("Received analysis request: {}", analysisRequest);
    var analysis = fullAnalysisService.processFullAnalysisRequest(analysisRequest);
    log.info("Performed analysis: {}", analysis);
    var analysisCompletionEvent = AnalysisCompleteEvent.builder()
        .fullAnalysis(analysis)
        .requestId(analysisRequest.requestId())
        .chatId(analysisRequest.user().getTelegramId())
        .build();
    eventPublisher.publishEvent(analysisCompletionEvent);
  }
}
