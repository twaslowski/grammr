package com.grammr.benchmark;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.grammr.annotation.BenchmarkTest;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.service.language.translation.semantic.OpenAISemanticTranslationService;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

@BenchmarkTest
public class OpenAISemanticTranslationBenchmarkTest extends AbstractBenchmarkTest {

  @Autowired
  protected OpenAISemanticTranslationService semanticTranslationService;

  @ParameterizedTest
  @MethodSource("providePhrasePairs")
  void shouldCreateSemanticTranslation(String sourcePhrase, String translatedPhrase, LanguageCode from, LanguageCode to) {
    var semanticTranslation = semanticTranslationService.createSemanticTranslation(sourcePhrase, from, to);
    assertThat(semanticTranslation.getSourcePhrase()).isEqualTo(sourcePhrase);
    assertThat(semanticTranslation.getTranslatedPhrase()).isEqualTo(translatedPhrase);
  }

  private static Stream<Arguments> providePhrasePairs() {
    return Stream.of(
        Arguments.of("Wie geht es dir?", "How are you doing?", LanguageCode.DE, LanguageCode.EN),
        Arguments.of("как у тебя дела?", "How are you doing?", LanguageCode.RU, LanguageCode.EN)
    );
  }
}
