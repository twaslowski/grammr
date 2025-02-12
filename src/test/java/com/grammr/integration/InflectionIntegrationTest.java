package com.grammr.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.port.dto.InflectionsRequest;
import com.grammr.domain.value.language.Token;
import com.grammr.domain.value.language.TokenMorphology;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@IntegrationTest
@AutoConfigureMockMvc
public class InflectionIntegrationTest extends IntegrationTestBase {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @SneakyThrows
  void shouldRetrieveInflectionsForWord() {
    String lemma = "дело";
    var morphology = TokenMorphology.builder().lemma(lemma).partOfSpeechTag(PartOfSpeechTag.NOUN).build();
    var token = Token.builder()
        .text("дела")
        .morphology(morphology)
        .build();
    var request = new InflectionsRequest(LanguageCode.RU, token);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/inflection")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lemma").value("дело"))
        .andExpect(jsonPath("$.inflections").isArray())
        .andExpect(jsonPath("$.partOfSpeech").value("NOUN"))
        .andReturn();
  }
}
