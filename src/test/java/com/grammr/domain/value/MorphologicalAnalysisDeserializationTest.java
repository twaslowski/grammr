package com.grammr.domain.value;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.config.ObjectMapperConfiguration;
import com.grammr.domain.value.language.MorphologicalAnalysis;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class MorphologicalAnalysisDeserializationTest {

  private final ObjectMapper objectMapper = new ObjectMapperConfiguration().objectMapper();

  @Test
  @SneakyThrows
  void shouldDeserialize() {
    var event = """
        {
          "source_phrase": "как у тебя дела?",
          "request_id": "0f4fb5b9-03e4-4bc5-a239-62453da28d01",
          "tokens": [
            {
              "text": "как",
              "lemma": "как",
              "pos": "SCONJ",
              "features": []
            },
            {
              "text": "тебя",
              "lemma": "тебя",
              "pos": "PRON",
              "features": [{
                "type": "CASE",
                "value": "GEN"
              },
              {
                "type": "NUMBER",
                "value": "SING"
              }]
            },
            {
              "text": "дела",
              "lemma": "дело",
              "pos": "NOUN",
              "features": [{
                "type": "GENDER",
                "value": "MALE"
              },
              {
                "type": "CASE",
                "value": "NOM"
              }]
            }
          ]
        }
        """;
    objectMapper.readValue(event, MorphologicalAnalysis.class);
  }
}
