package com.grammr.config.openai;

import io.github.sashirestela.openai.SimpleOpenAI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Deprecated
public class SimpleOpenAIConfiguration {

  @Bean
  public SimpleOpenAI openAIClient(
      @Value("${openai.api-key}") String apiKey
  ) {
    return SimpleOpenAI.builder()
        .apiKey(apiKey)
        .build();
  }
}
