package com.grammr.language.service.v1;

import com.grammr.config.value.LanguageConfiguration;
import com.grammr.domain.entity.Paradigm;
import com.grammr.domain.exception.InflectionNotAvailableException;
import com.grammr.domain.exception.ResourceNotFoundException;
import com.grammr.language.controller.v1.dto.InflectionsRequest;
import com.grammr.language.controller.v1.dto.ParadigmDto;
import com.grammr.language.port.InflectionPort;
import com.grammr.repository.ParadigmRepository;
import java.util.UUID;
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

  public ParadigmDto inflect(InflectionsRequest inflectionRequest) {
    return getParadigm(inflectionRequest).toDTO();
  }

  public Paradigm getParadigm(InflectionsRequest inflectionRequest) {
    return paradigmRepository.findByLemmaAndPartOfSpeechAndLanguageCode(
        inflectionRequest.lemma(),
        inflectionRequest.partOfSpeechTag(),
        inflectionRequest.languageCode()
    ).orElseGet(() -> retrieveParadigm(inflectionRequest));
  }

  public Paradigm getParadigm(UUID paradigmId) {
    return paradigmRepository.findById(paradigmId)
        .orElseThrow(() -> new ResourceNotFoundException(paradigmId.toString()));
  }

  private Paradigm retrieveParadigm(InflectionsRequest inflectionRequest) {
    if (languageConfiguration.isInflectionAvailable(inflectionRequest.languageCode())) {
      Paradigm paradigm = inflectionPort.retrieveInflections(inflectionRequest);
      paradigm.setLanguageCode(inflectionRequest.languageCode());
      return paradigmRepository.save(paradigm);
    } else {
      throw new InflectionNotAvailableException(inflectionRequest.languageCode());
    }
  }
}
