package com.grammr.integration;

import static com.grammr.domain.enums.PartOfSpeechTag.NOUN;
import static com.grammr.domain.enums.PartOfSpeechTag.PRON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.entity.Paradigm;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.value.language.Inflection;
import com.grammr.language.controller.v1.dto.InflectionsRequest;
import com.grammr.repository.ParadigmRepository;
import java.util.List;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@IntegrationTest
class InflectionIntegrationTest extends IntegrationTestBase {

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
  void shouldRetrieveParadigm() {
    var paradigm = paradigmRepository.save(Paradigm.builder()
        .partOfSpeech(PartOfSpeechTag.NOUN)
        .languageCode(LanguageCode.DE)
        .inflections(List.of(
            new Inflection("nominative", "singular", Set.of()),
            new Inflection("genitive", "singular", Set.of())
        ))
        .lemma("test")
        .build());

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/inflection/%s".formatted(paradigm.getId())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.paradigmId").value(paradigm.getId().toString()));
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
