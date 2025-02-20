package com.grammr.config.value;

import static org.assertj.core.api.Assertions.assertThat;

import com.grammr.domain.enums.LanguageCode;
import java.util.List;
import org.junit.jupiter.api.Test;

class LanguageConfigurationTest {

  @Test
  void shouldReturnFalseIfNoInflectionConfigurationProvidedForLanguage() {
    MorphologyConfiguration morphologyConfiguration = MorphologyConfiguration.builder().build();
    Language language = Language.builder()
        .code(LanguageCode.EN)
        .morphology(morphologyConfiguration)
        .build();

    var languageConfiguration = new LanguageConfiguration(List.of(language));
    assertThat(languageConfiguration.isInflectionAvailable(LanguageCode.EN)).isFalse();
  }
}