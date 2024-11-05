package com.grammr.benchmark;

import com.grammr.annotation.BenchmarkTest;
import com.grammr.domain.enums.LanguageCode;
import java.util.stream.Stream;
import com.grammr.domain.value.LanguageRecognition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

@BenchmarkTest
public class LanguageRecognitionBenchmarkTest extends AbstractBenchmarkTest {

  @ParameterizedTest
  @MethodSource("providePhraseLanguageCodePairs")
  void shouldCorrectlyRecognizeKnownLanguagePhrases(String phrase, LanguageCode languageCode) {
    assertThat(languageRecognitionService.recognizeLanguage(phrase)).isEqualTo(new LanguageRecognition(languageCode));
  }

  private static Stream<Arguments> providePhraseLanguageCodePairs() {
    return Stream.of(
        Arguments.of("How are you doing?", LanguageCode.EN),
        Arguments.of("Wie geht es dir?", LanguageCode.DE),
        Arguments.of("как у тебя дела?", LanguageCode.RU)
    );
  }
}
