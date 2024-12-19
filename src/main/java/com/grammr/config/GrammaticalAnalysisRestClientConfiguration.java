package com.grammr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class GrammaticalAnalysisRestClientConfiguration {

  private final ObjectMapper objectMapper;

  @Bean
  public RestClient restClient() {
    return RestClient.builder()
        .messageConverters(this::addObjectMapperToExistingJacksonConverter) // removes the existing JacksonConverter
        .build();
  }

  private void addObjectMapperToExistingJacksonConverter(List<HttpMessageConverter<?>> converters) {
    for (var converter : converters) {
      if (converter instanceof MappingJackson2HttpMessageConverter) {
        ((MappingJackson2HttpMessageConverter) converter).setObjectMapper(objectMapper);
      }
    }
  }
}
