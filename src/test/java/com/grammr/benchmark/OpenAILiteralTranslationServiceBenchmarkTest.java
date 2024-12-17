package com.grammr.benchmark;

import static org.assertj.core.api.Assertions.assertThat;

import com.grammr.annotation.BenchmarkTest;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.AnalysisComponentRequest;
import com.grammr.service.TokenService;
import com.grammr.service.language.translation.literal.OpenAILiteralTranslationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@BenchmarkTest
class OpenAILiteralTranslationServiceBenchmarkTest extends AbstractBenchmarkTest {

  @Autowired
  private OpenAILiteralTranslationService openAILiteralTranslationService;

  @Autowired
  private TokenService tokenService;

  @Test
  void benchmarkOpenAILiteralTranslation() {
    var phrase = "Wie geht es dir?";
    var tokens = tokenService.tokenize(phrase);

    var analysisComponentRequest = AnalysisComponentRequest.builder()
        .phrase(phrase)
        .sourceLanguage(LanguageCode.DE)
        .tokens(tokens)
        .build();

    var literalTranslation = openAILiteralTranslationService.createAnalysisComponent(analysisComponentRequest);
    var translatedTokens = literalTranslation.getTokenTranslations();

    assertThat(translatedTokens.size()).isEqualTo(4);
    assertThat(translatedTokens.getFirst().getSource()).isEqualTo("Wie");
    assertThat(translatedTokens.getFirst().getTranslation()).isEqualToIgnoringCase("How");

    assertThat(translatedTokens.get(1).getSource()).isEqualToIgnoringCase("geht");
    assertThat(translatedTokens.get(2).getSource()).isEqualToIgnoringCase("es");
    assertThat(translatedTokens.get(3).getSource()).isEqualToIgnoringCase("dir");
  }
}