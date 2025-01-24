package com.grammr.config;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.grammr.config.value.LanguageConfiguration;
import java.io.File;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LanguagesConfiguration {

  @Value("${grammr.languages.mount-path}")
  private String mountPath;

  @Bean
  @SneakyThrows
  public LanguageConfiguration languages() {
    ObjectMapper objectMapper = YAMLMapper.builder()
        .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        .build();
    return objectMapper.readValue(new File(mountPath), LanguageConfiguration.class);
  }
}
