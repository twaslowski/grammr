package com.grammr.integration;

import static com.grammr.domain.enums.PartOfSpeechTag.NOUN;
import static com.grammr.domain.enums.PartOfSpeechTag.PRON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.language.controller.dto.InflectionsRequest;
import com.grammr.repository.ParadigmRepository;
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

  @Autowired
  private ParadigmRepository paradigmRepository;

  @Test
  @SneakyThrows
  void shouldRetrieveInflectionsForWord() {
    var request = new InflectionsRequest(LanguageCode.RU, "дело", NOUN);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/inflection")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lemma").value("дело"))
        .andExpect(jsonPath("$.inflections").isArray())
        .andExpect(jsonPath("$.partOfSpeech").value("NOUN"))
        .andReturn();

    assertThat(paradigmRepository.findByLemmaAndPartOfSpeechAndLanguageCode("дело", NOUN, LanguageCode.RU))
        .isPresent();
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestForUnsupportedPartOfSpeech() {
    var request = new InflectionsRequest(LanguageCode.RU, "дело", PRON);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/inflection")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("Inflections are not available for pronouns in Russian"))
        .andReturn();
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestForLanguageWithNoInflectionsAvailable() {
    var request = new InflectionsRequest(LanguageCode.EN, "дело", NOUN);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/inflection")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("Inflections are not available in English"))
        .andReturn();
  }
}
