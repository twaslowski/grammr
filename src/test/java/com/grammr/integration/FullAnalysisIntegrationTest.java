package com.grammr.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.event.AnalysisRequest;
import com.grammr.domain.value.language.Token;
import com.grammr.domain.value.language.TokenTranslation;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@IntegrationTest
@AutoConfigureMockMvc
public class FullAnalysisIntegrationTest extends IntegrationTestBase {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @SneakyThrows
  void shouldPerformCompleteAnalysis() {
    var sourcePhrase = "Ich lerne heute Deutsch";
    var phraseTranslation = "I am learning German today";
    var tokens = tokenService.tokenize(sourcePhrase);
    var words = tokens.stream().map(Token::text).toList();

    mockLanguageRecognition(sourcePhrase, LanguageCode.DE);
    mockSemanticTranslation(sourcePhrase, phraseTranslation, LanguageCode.EN);

    for (String word : words) {
      mockTokenTranslation(sourcePhrase, word, new TokenTranslation(word, "someTranslation"));
    }

    // given a text update
    var analysisRequest = AnalysisRequest.builder()
        .userLanguageLearned(LanguageCode.DE)
        .userLanguageSpoken(LanguageCode.EN)
        .phrase(sourcePhrase)
        .performSemanticTranslation(true)
        .build();

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/analysis")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(analysisRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sourcePhrase").value(sourcePhrase))
        .andExpect(jsonPath("$.semanticTranslation.translatedPhrase").value(phraseTranslation))
        .andExpect(jsonPath("$.analyzedTokens").isArray())
        .andExpect(jsonPath("$.analyzedTokens[*].morphology").exists())
        .andExpect(jsonPath("$.analyzedTokens[*].translation").exists())
        .andReturn();
  }

  @Test
  @SneakyThrows
  void shouldPerformCompleteAnalysisWhenReceivingPhraseInUserSpokenLanguage() {
    var sourcePhrase = "I am learning German today";
    var translation = "Ich lerne heute Deutsch";

    var tokens = tokenService.tokenize(translation);
    var words = tokens.stream().map(Token::text).toList();

    mockLanguageRecognition(sourcePhrase, LanguageCode.EN);
    mockSemanticTranslation(sourcePhrase, translation, LanguageCode.DE);
    for (String word : words) {
      mockTokenTranslation(translation, word, new TokenTranslation(word, "someTranslation"));
    }

    var request = AnalysisRequest.builder()
        .userLanguageLearned(LanguageCode.DE)
        .userLanguageSpoken(LanguageCode.EN)
        .phrase(sourcePhrase)
        .performSemanticTranslation(true)
        .build();

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/analysis")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sourcePhrase").value(translation))
        .andExpect(jsonPath("$.semanticTranslation.translatedPhrase").value(translation))
        .andExpect(jsonPath("$.analyzedTokens").isArray())
        .andExpect(jsonPath("$.analyzedTokens[*].morphology").exists())
        .andExpect(jsonPath("$.analyzedTokens[*].translation").exists())
        .andReturn();
  }
}
