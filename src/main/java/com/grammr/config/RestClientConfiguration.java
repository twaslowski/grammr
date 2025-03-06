package com.grammr.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfiguration {

  private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

  @Bean
  public RestClient restClient() {
    return RestClient.builder()
        .messageConverters(this::customizeConverters)
        .build();
  }

  private void customizeConverters(List<HttpMessageConverter<?>> converters) {
    converters.clear();
    converters.add(mappingJackson2HttpMessageConverter);
  }
}
