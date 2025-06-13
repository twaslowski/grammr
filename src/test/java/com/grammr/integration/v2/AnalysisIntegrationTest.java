package com.grammr.integration.v2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.integration.IntegrationTestBase;
import com.grammr.language.controller.v2.dto.AnalysisRequest;
import com.grammr.repository.AnalysisRepository;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@IntegrationTest
public class AnalysisIntegrationTest extends IntegrationTestBase {

  @Autowired
  private AnalysisRepository analysisRepository;

  @BeforeEach
  void setup() {
    analysisRepository.deleteAll();
  }

  @Test
  @SneakyThrows
  void shouldSaveAnalysis() {
    var request = new AnalysisRequest("Wie geht es dir?", LanguageCode.DE);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/analysis")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.phrase").value(request.phrase()))
        .andExpect(jsonPath("$.sourceLanguage").value(request.languageCode().toString()))
        .andExpect(jsonPath("$.analysedTokens").isArray())
        .andExpect(jsonPath("$.analysedTokens[*].morphology").exists())
        .andReturn();

    var storedAnalysis = analysisRepository.findAll().getFirst();
    assertThat(storedAnalysis.getPhrase()).isEqualTo(request.phrase());
    assertThat(storedAnalysis.getSourceLanguage()).isEqualTo(request.languageCode());
    assertThat(storedAnalysis.getAnalysedTokens()).allSatisfy(token -> {
      assertThat(token.translation()).isNull();
      assertThat(token.morphology()).isNotNull();
    });

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/analysis/%s".formatted(storedAnalysis.getAnalysisId())))
        .andExpect(status().isOk());
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/analysis/%s".formatted(UUID.randomUUID().toString())))
        .andExpect(status().isNotFound());
  }
}
