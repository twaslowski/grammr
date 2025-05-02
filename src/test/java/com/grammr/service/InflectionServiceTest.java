package com.grammr.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.grammr.config.value.LanguageConfiguration;
import com.grammr.domain.entity.Paradigm;
import com.grammr.port.dto.InflectionsRequest;
import com.grammr.port.outbound.InflectionPort;
import com.grammr.repository.ParadigmRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InflectionServiceTest {

  @InjectMocks
  private InflectionService inflectionService;

  @Mock
  private LanguageConfiguration languageConfiguration;

  @Mock
  private ParadigmRepository paradigmRepository;

  @Mock
  private InflectionPort inflectionPort;

  @Test
  void shouldNotInteractWithInflectionPortWhenParadigmExists() {
    when(paradigmRepository.findByLemmaAndPartOfSpeechAndLanguageCode(any(), any(), any()))
        .thenReturn(Optional.of(Paradigm.builder().build()));

    // Act
    inflectionService.inflect(InflectionsRequest.builder().build());

    // Assert
    verify(inflectionPort, never()).retrieveInflections(any());
  }

  @Test
  void shouldInteractWithInflectionPortWhenParadigmDoesNotExist() {
    // Given
    var inflectionRequest = mock(InflectionsRequest.class);
    var paradigm = mock(Paradigm.class);

    when(paradigmRepository.findByLemmaAndPartOfSpeechAndLanguageCode(any(), any(), any()))
        .thenReturn(Optional.empty());
    when(languageConfiguration.isInflectionAvailable(any())).thenReturn(true);
    when(inflectionPort.retrieveInflections(any())).thenReturn(paradigm);
    when(paradigmRepository.save(any())).thenReturn(paradigm);

    // When
    inflectionService.inflect(inflectionRequest);

    // Then
    verify(inflectionPort).retrieveInflections(any());
  }
}
