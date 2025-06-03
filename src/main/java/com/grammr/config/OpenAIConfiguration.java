package com.grammr.config;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfiguration {

  @Value("${openai.api-key}")
  private String apiKey;

  @Bean
  public OpenAIClient client() {
    return OpenAIOkHttpClient.builder()
        .apiKey(apiKey)
        .build();
  }
}
