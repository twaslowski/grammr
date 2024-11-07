package com.grammr.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.language.LanguageRecognition;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class ObjectMapperTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @SneakyThrows
  void shouldMapLanguageRecognitionObjectToJsonProperly() {
    // given
    var languageRecognition = new LanguageRecognition(LanguageCode.DE);

    // when
    var json = objectMapper.writeValueAsString(languageRecognition);

    // then
    assertThat(json).isEqualTo("{\"languageCode\":\"DE\"}");
  }
}
