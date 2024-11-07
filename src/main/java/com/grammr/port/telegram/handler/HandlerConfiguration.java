package com.grammr.port.telegram.handler;

import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class HandlerConfiguration {

  private final TextUpdateHandler textUpdateHandler;

  @Bean
  public Collection<UpdateHandler> handlers() {
    return List.of(
        textUpdateHandler
    );
  }
}
