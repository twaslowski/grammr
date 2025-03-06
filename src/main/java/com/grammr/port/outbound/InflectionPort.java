package com.grammr.port.outbound;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import com.grammr.config.value.LanguageConfiguration;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.exception.InflectionNotAvailable;
import com.grammr.domain.value.language.Inflections;
import com.grammr.port.dto.InflectionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class InflectionPort {

  private final RestClient restClient;
  private final LanguageConfiguration languageConfiguration;

  public Inflections retrieveInflections(LanguageCode languageCode, InflectionRequest request) {
    var uri = languageConfiguration.getInflectionUri(languageCode);
    log.info("Inflecting word '{}'", request.lemma());
    try {
      return restClient
          .post()
          .uri(uri)
          .body(request)
          .retrieve()
          .body(Inflections.class);
    } catch (HttpClientErrorException e) {
      log.info("Inflections could not be performed for word {}, pos {}", request.lemma(), request.partOfSpeechTag());
      if (e.getStatusCode() == UNPROCESSABLE_ENTITY) {
        throw new InflectionNotAvailable(languageCode, request.partOfSpeechTag());
      }
      throw e;
    } catch (Exception e) {
      log.error("Error retrieving inflections for word: {}", request.lemma(), e);
      throw e;
    }
  }
}
