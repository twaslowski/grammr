package com.grammr.config;

import com.grammr.domain.event.AnalysisCompleteEvent;
import com.grammr.domain.event.AnalysisRequestEvent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.context.annotation.Bean;

public class QueueConfig {

  @Bean
  public BlockingQueue<AnalysisRequestEvent> analysisRequestEventQueue() {
    return new LinkedBlockingQueue<>();
  }

  @Bean
  public BlockingQueue<AnalysisCompleteEvent> analysisCompleteEventQueue() {
    return new LinkedBlockingQueue<>();
  }
}
