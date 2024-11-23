package com.grammr.config;

import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfiguration {

  @Bean
  public ObjectMapper objectMapper() {
    // registerFeatureDeserializer(objectMapper);
    return JsonMapper.builder()
        .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
        .build();
  }

  private void registerFeatureDeserializer(ObjectMapper objectMapper) {
    var module = new SimpleModule();
    module.addDeserializer(Map.class, new FeatureDeserializer());
    objectMapper.registerModule(module);
  }
}
