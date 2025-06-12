package com.grammr.language.port;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import com.grammr.config.value.LanguageConfiguration;
import com.grammr.domain.entity.Paradigm;
import com.grammr.domain.exception.InflectionNotAvailableException;
import com.grammr.domain.value.language.ParadigmDTO;
import com.grammr.language.controller.v1.dto.InflectionsRequest;
import java.util.Optional;
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

  public Paradigm retrieveInflections(InflectionsRequest request) {
    var uri = languageConfiguration.getInflectionUri(request.languageCode());
    log.info("Inflecting word '{}'", request.lemma());
    try {
      ParadigmDTO paradigmDTO = restClient
          .post()
          .uri(uri)
          .body(request)
          .retrieve()
          .body(ParadigmDTO.class);

      return Optional.ofNullable(paradigmDTO)
          .map(Paradigm::from)
          .orElseThrow(() -> new InflectionNotAvailableException(request.languageCode(), request.partOfSpeechTag()));
    } catch (HttpClientErrorException e) {
      log.info("Inflections could not be performed for word {}, pos {}", request.lemma(), request.partOfSpeechTag());
      if (e.getStatusCode() == UNPROCESSABLE_ENTITY) {
        throw new InflectionNotAvailableException(request.languageCode(), request.partOfSpeechTag());
      }
      throw e;
    } catch (Exception e) {
      log.error("Error retrieving inflections for word: {}", request.lemma(), e);
      throw e;
    }
  }
}
