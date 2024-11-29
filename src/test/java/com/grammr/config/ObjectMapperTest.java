package com.grammr.config;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.event.MorphologicalAnalysisRequest;
import com.grammr.domain.value.language.LanguageRecognition;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONCompareMode;

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
    assertEquals("{\"languageCode\":\"DE\"}",
        json,
        JSONCompareMode.LENIENT
    );
  }

  @Test
  @SneakyThrows
  void shouldSerializeGrammaticalAnalysisRequest() {
    var phrase = "Foo";
    var request = MorphologicalAnalysisRequest.from(phrase);

    var json = objectMapper.writeValueAsString(request);
    assertEquals("{"
            + "\"phrase\":\"Foo\","
            + "\"requestId\":\"" + request.requestId()
            + "\"}",
        json,
        JSONCompareMode.LENIENT
    );
  }
}
