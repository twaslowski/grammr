package com.grammr.port;

import com.grammr.common.AbstractConsumer;
import com.grammr.domain.event.AnalysisCompleteEvent;
import com.grammr.domain.event.AnalysisRequestEvent;
import com.grammr.service.AnalysisRequestService;
import java.util.concurrent.BlockingQueue;
import org.springframework.stereotype.Service;

@Service
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
    var analysisCompletionEvent = AnalysisCompleteEvent.builder()
        .analysis(analysis)
        .requestId(analysisRequest.requestId())
        .chatId(analysisRequest.chatId())
        .build();
    analysisCompleteEventQueue.add(analysisCompletionEvent);
  }
}
