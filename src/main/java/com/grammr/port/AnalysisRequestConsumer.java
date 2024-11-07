package com.grammr.port;

import com.grammr.common.AbstractConsumer;
import com.grammr.domain.event.AnalysisCompleteEvent;
import com.grammr.domain.event.AnalysisRequestEvent;
import com.grammr.service.AnalysisRequestService;
import java.util.concurrent.BlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AnalysisRequestConsumer extends AbstractConsumer<AnalysisRequestEvent> {

  private final AnalysisRequestService analysisRequestService;
  private final BlockingQueue<AnalysisCompleteEvent> analysisCompleteEventQueue;

  public AnalysisRequestConsumer(BlockingQueue<AnalysisRequestEvent> analysisRequestQueue,
                                 BlockingQueue<AnalysisCompleteEvent> analysisCompleteEventQueue,
                                 AnalysisRequestService analysisRequestService) {
    super(analysisRequestQueue);
    this.analysisCompleteEventQueue = analysisCompleteEventQueue;
    this.analysisRequestService = analysisRequestService;
  }

  @Override
  protected void handleItem(AnalysisRequestEvent analysisRequest) {
    var analysis = analysisRequestService.processAnalysisRequest(analysisRequest);
    log.info("Performed analysis: {}", analysis);
    var analysisCompletionEvent = AnalysisCompleteEvent.builder()
        .analysis(analysis)
        .requestId(analysisRequest.requestId())
        .chatId(analysisRequest.chatId())
        .build();
    analysisCompleteEventQueue.add(analysisCompletionEvent);
  }
}
