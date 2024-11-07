package com.grammr.port.telegram.config;

import com.grammr.port.telegram.dto.response.TelegramResponse;
import com.grammr.port.telegram.dto.update.TelegramUpdate;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramQueueConfiguration {

  @Bean
  public BlockingQueue<TelegramUpdate> incomingMessageQueue() {
    return new LinkedBlockingQueue<>();
  }

  @Bean
  public BlockingQueue<TelegramResponse> outgoingMessageQueue() {
    return new LinkedBlockingQueue<>();
  }
}
