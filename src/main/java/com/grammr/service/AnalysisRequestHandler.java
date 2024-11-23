package com.grammr.service;

import com.grammr.common.AbstractConsumer;
import com.grammr.domain.event.FullAnalysisCompleteEvent;
import com.grammr.domain.event.FullAnalysisRequestEvent;
import java.util.concurrent.BlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AnalysisRequestHandler extends AbstractConsumer<FullAnalysisRequestEvent> {

  private final AnalysisRequestService analysisRequestService;
  private final BlockingQueue<FullAnalysisCompleteEvent> fullAnalysisCompleteEventQueue;

  public AnalysisRequestHandler(BlockingQueue<FullAnalysisRequestEvent> analysisRequestQueue,
                                BlockingQueue<FullAnalysisCompleteEvent> fullAnalysisCompleteEventQueue,
                                AnalysisRequestService analysisRequestService) {
    super(analysisRequestQueue);
    this.fullAnalysisCompleteEventQueue = fullAnalysisCompleteEventQueue;
    this.analysisRequestService = analysisRequestService;
  }

  @Override
  protected void handleItem(FullAnalysisRequestEvent analysisRequest) {
    var analysis = analysisRequestService.processFullAnalysisRequest(analysisRequest);
    log.info("Performed analysis: {}", analysis);
    var analysisCompletionEvent = FullAnalysisCompleteEvent.builder()
        .fullAnalysis(analysis)
        .requestId(analysisRequest.requestId())
        .chatId(analysisRequest.chatId())
        .build();
    fullAnalysisCompleteEventQueue.add(analysisCompletionEvent);
  }
}
