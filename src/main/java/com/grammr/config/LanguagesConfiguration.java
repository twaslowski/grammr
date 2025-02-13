package com.grammr.config;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.grammr.config.value.Language;
import com.grammr.config.value.LanguageConfiguration;
import java.io.File;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
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
    var languageConfiguration = objectMapper.readValue(new File(mountPath), LanguageConfiguration.class);
    log.info("Loaded language configuration from '{}'", mountPath);
    log.info("Running with the following languages: {}", languageConfiguration.languages().stream()
        .map(Language::code).collect(Collectors.toSet()));
    return languageConfiguration;
  }
}
