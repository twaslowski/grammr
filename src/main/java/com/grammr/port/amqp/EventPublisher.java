package com.grammr.port.amqp;

import com.grammr.config.QueueConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
public class EventPublisher {

  private final RabbitTemplate rabbitTemplate;

  @Scheduled(fixedDelay = 1000, initialDelay = 500)
  public void send() {
    String message = "Hello World!";
    rabbitTemplate.convertAndSend(QueueConfiguration.EXCHANGE_NAME, "routing.key", message);
    log.info(" [x] Sent '{}'", message);
  }
}
