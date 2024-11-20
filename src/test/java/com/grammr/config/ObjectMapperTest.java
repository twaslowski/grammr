package com.grammr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.event.GrammaticalAnalysisRequest;
import com.grammr.domain.value.language.LanguageRecognition;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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

  @Test
  @SneakyThrows
  void shouldSerializeGrammaticalAnalysisRequest() {
    var phrase = "Foo";
    var request = GrammaticalAnalysisRequest.from(phrase);

    var json = objectMapper.writeValueAsString(request);
    assertThat(json).isEqualTo("{\"phrase\":\"Foo\",\"requestId\":\"" + request.requestId() + "\"}");
  }
}
