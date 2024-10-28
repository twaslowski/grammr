package com.grammr.port.telegram.handler;

import com.grammr.port.telegram.handler.command.HelpHandler;
import com.grammr.port.telegram.handler.command.StartHandler;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class HandlerConfiguration {

  private final StartHandler startHandler;
  private final HelpHandler helpHandler;

  @Bean
  public Collection<UpdateHandler> handlers() {
    return List.of(
        startHandler,
        helpHandler
        );
  }
}
