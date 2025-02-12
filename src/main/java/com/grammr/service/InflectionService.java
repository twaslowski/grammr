package com.grammr.service;

import com.grammr.config.value.LanguageConfiguration;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.exception.InflectionNotAvailableForLanguageException;
import com.grammr.domain.value.language.Inflections;
import com.grammr.domain.value.language.Token;
import com.grammr.port.dto.InflectionRequest;
import com.grammr.port.outbound.InflectionPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InflectionService {

  private final LanguageConfiguration languageConfiguration;
  private final InflectionPort inflectionPort;

  public Inflections inflect(LanguageCode languageCode, Token token) {
    if (languageConfiguration.isInflectionAvailable(languageCode)) {
      var inflectionRequest = InflectionRequest.from(token);
      return inflectionPort.retrieveInflections(languageCode, inflectionRequest);
    } else {
      throw new InflectionNotAvailableForLanguageException(languageCode);
    }
  }
}
