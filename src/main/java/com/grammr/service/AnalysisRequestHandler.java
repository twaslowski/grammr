package com.grammr.service;

import com.grammr.common.AbstractConsumer;
import com.grammr.domain.event.AnalysisCompleteEvent;
import com.grammr.domain.event.AnalysisRequestEvent;
import java.util.concurrent.BlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AnalysisRequestHandler extends AbstractConsumer<AnalysisRequestEvent> {

  private final AnalysisRequestService analysisRequestService;
  private final BlockingQueue<AnalysisCompleteEvent> analysisCompleteEventQueue;

  public AnalysisRequestHandler(BlockingQueue<AnalysisRequestEvent> analysisRequestQueue,
                                BlockingQueue<AnalysisCompleteEvent> analysisCompleteEventQueue,
                                AnalysisRequestService analysisRequestService) {
    super(analysisRequestQueue);
    this.analysisCompleteEventQueue = analysisCompleteEventQueue;
    this.analysisRequestService = analysisRequestService;
  }

  @Override
  protected void handleItem(AnalysisRequestEvent analysisRequest) {
    var analysis = analysisRequestService.processFullAnalysisRequest(analysisRequest);
    log.info("Performed analysis: {}", analysis);
    var analysisCompletionEvent = AnalysisCompleteEvent.builder()
        .fullAnalysis(analysis)
        .requestId(analysisRequest.requestId())
        .chatId(analysisRequest.user().getTelegramId())
        .build();
    analysisCompleteEventQueue.add(analysisCompletionEvent);
  }
}
