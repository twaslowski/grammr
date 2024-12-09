package com.grammr.telegram.handler;

import com.grammr.telegram.handler.command.StartHandler;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class HandlerConfiguration {

  private final TextUpdateHandler textUpdateHandler;
  private final StartHandler startHandler;

  @Bean
  public Collection<UpdateHandler> handlers() {
    return List.of(
        startHandler,
        textUpdateHandler
    );
  }
}
