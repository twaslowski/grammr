package com.grammr.service;

import com.grammr.config.value.LanguageConfiguration;
import com.grammr.domain.entity.Paradigm;
import com.grammr.domain.exception.InflectionNotAvailable;
import com.grammr.domain.value.language.ParadigmDTO;
import com.grammr.port.dto.InflectionsRequest;
import com.grammr.port.outbound.InflectionPort;
import com.grammr.repository.ParadigmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InflectionService {

  private final LanguageConfiguration languageConfiguration;
  private final ParadigmRepository paradigmRepository;
  private final InflectionPort inflectionPort;

  public ParadigmDTO inflect(InflectionsRequest inflectionRequest) {
    var lemma = inflectionRequest.lemma();
    var partOfSpeechTag = inflectionRequest.partOfSpeechTag();
    var languageCode = inflectionRequest.languageCode();

    return paradigmRepository.findByLemmaAndPartOfSpeechAndLanguageCode(lemma, partOfSpeechTag, languageCode)
        .orElseGet(() -> retrieveParadigm(inflectionRequest))
        .toDTO();
  }

  private Paradigm retrieveParadigm(InflectionsRequest inflectionRequest) {
    if (languageConfiguration.isInflectionAvailable(inflectionRequest.languageCode())) {
      Paradigm paradigm = inflectionPort.retrieveInflections(inflectionRequest);
      paradigm.setLanguageCode(inflectionRequest.languageCode());
      return paradigmRepository.save(paradigm);
    } else {
      throw new InflectionNotAvailable(inflectionRequest.languageCode());
    }
  }
}
