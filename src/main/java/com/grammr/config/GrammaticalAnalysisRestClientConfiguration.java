package com.grammr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GrammaticalAnalysisRestClientConfiguration {

  @Bean
  public RestClient restClient() {
    return RestClient.builder()
        .baseUrl("http://localhost:8000")
        .build();
  }
}
