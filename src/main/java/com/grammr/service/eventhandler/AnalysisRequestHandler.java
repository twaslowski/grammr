package com.grammr.service.eventhandler;

import com.grammr.domain.event.AnalysisCompleteEvent;
import com.grammr.domain.event.AnalysisRequest;
import com.grammr.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Deprecated
public class AnalysisRequestHandler {

  /**
   * Handles events from the Telegram Client.
   * The Telegram Client will eventually be moved to a separate service and communicate with
   * the core application via its REST interface, so this should no longer be used.
   */

  private final AnalysisService analysisService;
  private final ApplicationEventPublisher eventPublisher;

  @Async
  @EventListener
  public void handleAnalysisRequest(AnalysisRequest analysisRequest) {
    log.info("Received analysis request: {}", analysisRequest);

    var analysis = analysisService.processFullAnalysisRequest(analysisRequest);
    log.info("Performed analysis: {}", analysis);
    var analysisCompletionEvent = AnalysisCompleteEvent.builder()
        .fullAnalysis(analysis)
        .requestId(analysisRequest.requestId())
        .build();
    eventPublisher.publishEvent(analysisCompletionEvent);
  }
}
