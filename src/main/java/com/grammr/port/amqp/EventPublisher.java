package com.grammr.port.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.domain.event.MorphologicalAnalysisRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
public class EventPublisher {

  private final RabbitTemplate rabbitTemplate;
  private final ObjectMapper objectMapper;

  @Value("${analysis.messaging.exchange}")
  private String morphologicalAnalysisRequestExchange;

  @Value("${analysis.messaging.routing-key.requested}")
  private String morphologicalAnalysisRequestRoutingKey;

  @SneakyThrows
  public void publish(MorphologicalAnalysisRequest request) {
    var string = objectMapper.writeValueAsString(request);
    var routingKey = morphologicalAnalysisRequestRoutingKey + "." + request.languageCode();
    log.info("Publishing morphological analysis request with requestId {}", request.requestId());
    rabbitTemplate.convertAndSend(morphologicalAnalysisRequestExchange, routingKey, string);
  }
}
