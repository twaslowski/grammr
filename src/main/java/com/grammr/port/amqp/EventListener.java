package com.grammr.port.amqp;

import com.grammr.config.QueueConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventListener {

  @RabbitListener(queues = QueueConfiguration.QUEUE_NAME)
  public void receive(String message) {
    log.info("Received message: {}", message);
  }
}
