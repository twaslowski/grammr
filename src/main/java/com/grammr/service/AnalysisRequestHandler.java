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

  private final FullAnalysisService fullAnalysisService;
  private final BlockingQueue<AnalysisCompleteEvent> analysisCompleteEventQueue;

  public AnalysisRequestHandler(BlockingQueue<AnalysisRequestEvent> analysisRequestQueue,
                                BlockingQueue<AnalysisCompleteEvent> analysisCompleteEventQueue,
                                FullAnalysisService fullAnalysisService) {
    super(analysisRequestQueue);
    this.analysisCompleteEventQueue = analysisCompleteEventQueue;
    this.fullAnalysisService = fullAnalysisService;
  }

  @Override
  protected void handleItem(AnalysisRequestEvent analysisRequest) {
    var analysis = fullAnalysisService.processFullAnalysisRequest(analysisRequest);
    log.info("Performed analysis: {}", analysis);
    var analysisCompletionEvent = AnalysisCompleteEvent.builder()
        .fullAnalysis(analysis)
        .requestId(analysisRequest.requestId())
        .chatId(analysisRequest.user().getTelegramId())
        .build();
    analysisCompleteEventQueue.add(analysisCompletionEvent);
  }
}
