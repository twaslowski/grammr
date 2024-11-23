package com.grammr.config;

import com.grammr.domain.event.FullAnalysisCompleteEvent;
import com.grammr.domain.event.FullAnalysisRequestEvent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnalysisEventQueueConfiguration {

  @Bean
  public BlockingQueue<FullAnalysisRequestEvent> analysisRequestEventQueue() {
    return new LinkedBlockingQueue<>();
  }

  @Bean
  public BlockingQueue<FullAnalysisCompleteEvent> analysisCompleteEventQueue() {
    return new LinkedBlockingQueue<>();
  }
}
