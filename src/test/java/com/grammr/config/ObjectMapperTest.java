package com.grammr.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.enums.features.Case;
import com.grammr.domain.enums.features.Feature;
import com.grammr.domain.enums.features.Person;
import com.grammr.language.controller.v1.dto.MorphologicalAnalysisRequest;
import com.grammr.domain.value.language.LanguageRecognition;
import com.grammr.domain.value.language.SemanticTranslation;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONCompareMode;

public class ObjectMapperTest {

  private final ObjectMapper objectMapper = new ObjectMapperConfiguration().objectMapper();

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
        JSONCompareMode.STRICT
    );
  }

  @Test
  @SneakyThrows
  void shouldNotSerializeCompletionTokensOnAIGeneratedContentClasses() {
    var languageRecognition = new LanguageRecognition(LanguageCode.DE);
    languageRecognition.setCompletionTokens(100);
    var json = objectMapper.writeValueAsString(languageRecognition);
    assertEquals("{\"languageCode\":\"DE\"}",
        json,
        JSONCompareMode.STRICT
    );
  }

  @Test
  @SneakyThrows
  void shouldSerializeGrammaticalAnalysisRequest() {
    var phrase = "Foo";
    var request = MorphologicalAnalysisRequest.from(phrase, LanguageCode.DE);

    var json = objectMapper.writeValueAsString(request);
    assertEquals("{"
            + "\"phrase\":\"Foo\","
            + "\"languageCode\":\"DE\","
            + "\"requestId\":\"" + request.requestId()
            + "\"}",
        json,
        JSONCompareMode.LENIENT
    );
  }

  @Test
  @SneakyThrows
  void shouldDeserializeSemanticTranslation() {
    var json = "{\"sourcePhrase\":\"How are you doing\",\"translatedPhrase\":\"Wie geht es dir?\"}";
    var translation = objectMapper.readValue(json, SemanticTranslation.class);
    assertThat(translation.getSourcePhrase()).isEqualTo("How are you doing");
    assertThat(translation.getTranslatedPhrase()).isEqualTo("Wie geht es dir?");
  }

  @Test
  @SneakyThrows
  void shouldDeserializeFeature() {
    var json = "{\"type\":\"CASE\", \"value\":\"NOM\"}";
    var feature = objectMapper.readValue(json, Feature.class);
    assertThat(feature.getEnumValue()).isEqualTo(Case.NOM);

    json = "{\"type\": \"PERSON\", \"value\": \"FIRST\"}";
    feature = objectMapper.readValue(json, Feature.class);
    assertThat(feature.getEnumValue()).isEqualTo(Person.FIRST);
  }
}
