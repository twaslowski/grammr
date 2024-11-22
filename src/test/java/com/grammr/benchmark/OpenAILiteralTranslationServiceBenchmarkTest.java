package com.grammr.benchmark;

import static org.assertj.core.api.Assertions.assertThat;

import com.grammr.annotation.BenchmarkTest;
import com.grammr.domain.value.language.TokenTranslation;
import com.grammr.service.language.translation.literal.OpenAILiteralTranslationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@BenchmarkTest
class OpenAILiteralTranslationServiceBenchmarkTest extends AbstractBenchmarkTest {

  @Autowired
  private OpenAILiteralTranslationService openAILiteralTranslationService;

  @Test
  void benchmarkOpenAILiteralTranslation() {
    var literalTranslation = openAILiteralTranslationService.createLiteralTranslation("Wie geht es dir?");
    assertThat(literalTranslation.sourcePhrase()).isEqualTo("Wie geht es dir?");
    assertThat(literalTranslation.tokenTranslations()).isNotEmpty();

    assertThat(literalTranslation.tokenTranslations()).contains(new TokenTranslation("Wie", "How"));
  }
}