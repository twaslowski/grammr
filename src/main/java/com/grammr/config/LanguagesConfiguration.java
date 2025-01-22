package com.grammr.config;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.grammr.config.value.Language;
import com.grammr.config.value.LanguageConfiguration;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LanguagesConfiguration {

  @Value("${grammr.languages.mount-path}")
  private String mountPath;

  @Bean
  public LanguageConfiguration languages() {
    ObjectMapper objectMapper = YAMLMapper.builder()
        .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        .build();
    return new LanguageConfiguration(loadLanguagesFromConfiguration(objectMapper));
  }

  @SneakyThrows
  private List<Language> loadLanguagesFromConfiguration(ObjectMapper objectMapper) {
    CollectionType typeReference = objectMapper.getTypeFactory()
        .constructCollectionType(ArrayList.class, Language.class);
    return objectMapper.readValue(new File(mountPath), typeReference);
  }
}
