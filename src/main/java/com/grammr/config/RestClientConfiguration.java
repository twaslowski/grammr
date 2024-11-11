package com.grammr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

public class RestClientConfiguration {

  @Bean
  public RestClient restClient() {
    return RestClient.create();
  }

}
