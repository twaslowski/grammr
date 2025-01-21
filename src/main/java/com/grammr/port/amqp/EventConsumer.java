package com.grammr.port.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.domain.value.language.MorphologicalAnalysis;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventConsumer {

  private final ObjectMapper objectMapper;

  @SneakyThrows
  @RabbitListener(queues = "${analysis.messaging.queue}")
  public void processMorphologicalAnalysisComplete(String body) {
    log.info("Received completion: {}", body);
    var result = objectMapper.readValue(body, MorphologicalAnalysis.class);
  }
}
