package com.grammr.integration;

import com.grammr.domain.event.AnalysisCompleteEvent;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Getter
@Component
public class EventAccumulator {

  private final List<AnalysisCompleteEvent> analysisCompleteEvents = new ArrayList<>();

  public void reset() {
    analysisCompleteEvents.clear();
  }

  @EventListener
  @Async
  public void saveEvent(AnalysisCompleteEvent analysisCompleteEvent) {
    analysisCompleteEvents.add(analysisCompleteEvent);
  }
}
