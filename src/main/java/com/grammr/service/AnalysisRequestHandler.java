package com.grammr.service;

import com.grammr.domain.entity.User;
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
  private final RequestService requestService;

  @Async
  @EventListener
  public void handleItem(AnalysisRequestEvent analysisRequest) {
    log.info("Received analysis request: {}", analysisRequest);

    if (!hasLanguageInformation(analysisRequest)) {
      log.warn("Received analysis request without language information, retrieving user: {}", analysisRequest);
      var user = retrieveUser(analysisRequest);
      analysisRequest = analysisRequest.withLanguageInformation(user);
    }

    var analysis = fullAnalysisService.processFullAnalysisRequest(analysisRequest);
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

  private User retrieveUser(AnalysisRequestEvent analysisRequest) {
    return requestService.retrieveUser(analysisRequest.requestId());
  }
}
