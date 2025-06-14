package com.grammr.config.value;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.exception.ConfigurationNotAvailableException;
import java.util.List;
import java.util.Optional;

public record LanguageConfiguration(
    List<Language> languages
) {

  public boolean isMorphologyAvailable(LanguageCode languageCode) {
    return languages.stream()
        .filter(language -> language.code().equals(languageCode))
        .map(Language::morphology)
        .map(MorphologyConfiguration::enabled)
        .findFirst()
        .orElse(false);
  }

  public boolean isInflectionAvailable(LanguageCode languageCode) {
    return languages.stream()
        .filter(language -> language.code().equals(languageCode))
        .findFirst()
        .flatMap(language -> Optional.ofNullable(language.inflection()))
        .map(InflectionConfiguration::enabled).orElse(false);
  }

  public String getMorphologyUri(LanguageCode languageCode) {
    return languages.stream()
        .filter(language -> language.code().equals(languageCode))
        .map(Language::morphology)
        .map(MorphologyConfiguration::uri)
        .findFirst()
        .orElseThrow(() -> new ConfigurationNotAvailableException("Morphological analysis not available for language: " + languageCode));
  }

  public String getInflectionUri(LanguageCode languageCode) {
    return languages.stream()
        .filter(language -> language.code().equals(languageCode))
        .map(Language::inflection)
        .map(InflectionConfiguration::uri)
        .findFirst()
        .orElseThrow(() -> new ConfigurationNotAvailableException("Inflections not available for language: " + languageCode));
  }
}
