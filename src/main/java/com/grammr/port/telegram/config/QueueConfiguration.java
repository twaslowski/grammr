package com.grammr.port.telegram.config;

import com.grammr.port.telegram.dto.response.TelegramResponse;
import com.grammr.port.telegram.dto.update.TelegramUpdate;
import java.util.LinkedList;
import java.util.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfiguration {

  @Bean
  public Queue<TelegramUpdate> incomingMessageQueue() {
    return new LinkedList<>();
  }

  @Bean
  public Queue<TelegramResponse> outgoingMessageQueue() {
    return new LinkedList<>();
  }
}
