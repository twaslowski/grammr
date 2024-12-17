package com.grammr.benchmark;

import static org.assertj.core.api.Assertions.assertThat;

import com.grammr.annotation.BenchmarkTest;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.AnalysisComponentRequest;
import com.grammr.domain.value.language.LanguageRecognition;
import com.grammr.service.language.recognition.OpenAILanguageRecognitionService;
import java.util.stream.Stream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

@BenchmarkTest
@Disabled
public class OpenAILanguageRecognitionBenchmarkTest extends AbstractBenchmarkTest {

  @Autowired
  protected OpenAILanguageRecognitionService languageRecognitionService;

  @ParameterizedTest
  @MethodSource("providePhraseLanguageCodePairs")
  void shouldCorrectlyRecognizeKnownLanguagePhrases(String phrase, LanguageCode languageCode) {
    var analysisComponentRequest = AnalysisComponentRequest.builder()
        .phrase(phrase)
        .build();
    assertThat(languageRecognitionService.createAnalysisComponent(analysisComponentRequest))
        .isEqualTo(new LanguageRecognition(languageCode));
  }

  private static Stream<Arguments> providePhraseLanguageCodePairs() {
    return Stream.of(
        Arguments.of("How are you doing?", LanguageCode.EN),
        Arguments.of("Wie geht es dir?", LanguageCode.DE),
        Arguments.of("как у тебя дела?", LanguageCode.RU),
        Arguments.of("fasdioufn asdnklj ert cdvsd", LanguageCode.UNSUPPORTED)
    );
  }
}
