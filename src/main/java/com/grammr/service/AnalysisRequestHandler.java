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

  private final AnalysisService analysisService;
  private final ApplicationEventPublisher eventPublisher;

  @Async
  @EventListener
  public void handleAnalysisRequest(AnalysisRequestEvent analysisRequest) {
    log.info("Received analysis request: {}", analysisRequest);

    if (!hasLanguageInformation(analysisRequest)) {
      log.warn("Received analysis request without language information, retrieving user: {}", analysisRequest);
      analysisRequest = analysisRequest.withoutLanguageInformation();
    }

    var analysis = analysisService.processFullAnalysisRequest(analysisRequest);
    log.info("Performed analysis: {}", analysis);
    var analysisCompletionEvent = AnalysisCompleteEvent.builder()
        .fullAnalysis(analysis)
        .requestId(analysisRequest.requestId())
        .build();
    eventPublisher.publishEvent(analysisCompletionEvent);
  }

  private boolean hasLanguageInformation(AnalysisRequestEvent analysisRequest) {
    return analysisRequest.userLanguageLearned() != null
        && analysisRequest.userLanguageSpoken() != null;
  }
}
