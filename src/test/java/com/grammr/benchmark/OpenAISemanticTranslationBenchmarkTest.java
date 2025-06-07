package com.grammr.benchmark;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.grammr.annotation.BenchmarkTest;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.AnalysisComponentRequest;
import com.grammr.language.service.translation.semantic.OpenAISemanticTranslationService;
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
  void shouldCreateSemanticTranslation(String sourcePhrase, String translatedPhrase, LanguageCode to) {
    var analysisComponentRequest = AnalysisComponentRequest.builder()
        .phrase(sourcePhrase)
        .targetLanguage(to)
        .build();
    var semanticTranslation = semanticTranslationService.createAnalysisComponent(analysisComponentRequest);
    assertThat(semanticTranslation.getSourcePhrase()).isEqualTo(sourcePhrase);
    assertThat(semanticTranslation.getTranslatedPhrase()).isEqualTo(translatedPhrase);
  }

  private static Stream<Arguments> providePhrasePairs() {
    return Stream.of(
        Arguments.of("Wie geht es dir?", "How are you doing?", LanguageCode.EN),
        Arguments.of("как у тебя дела?", "How are you doing?", LanguageCode.EN)
    );
  }
}
