package com.grammr.config;

import io.github.sashirestela.openai.SimpleOpenAI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class OpenAIClientConfiguration {

  @Bean
  public SimpleOpenAI client(
      @Value("${openai.api-key}") String apiKey,
      @Value("${openai.project-id}") String projectId
  ) {
    return SimpleOpenAI.builder()
        .apiKey(apiKey)
        .projectId(projectId)
        .build();
  }
}
