package com.grammr.port.rest;

import com.grammr.config.value.LanguageConfiguration;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.event.InflectionRequest;
import com.grammr.domain.value.language.Inflections;
import com.grammr.domain.value.language.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class InflectionPort {

  private final RestClient restClient;
  private final LanguageConfiguration languageConfiguration;

  public Inflections performInflections(LanguageCode languageCode, Token token) {
    var uri = languageConfiguration.getInflectionUri(languageCode);
    var request = InflectionRequest.from(token);
    log.info("Performing analysis for phrase '{}' at '{}'", token.text(), uri);
    try {
      return restClient
          .post()
          .uri(uri)
          .body(request)
          .retrieve()
          .body(Inflections.class);
    } catch (Exception e) {
      log.error("Error performing analysis for phrase: {}", request.lemma(), e);
      throw e;
    }
  }
}
