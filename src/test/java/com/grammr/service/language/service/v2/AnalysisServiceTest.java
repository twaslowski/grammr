package com.grammr.service.language.service.v2;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.grammr.domain.exception.ResourceNotFoundException;
import com.grammr.language.service.v2.AnalysisService;
import com.grammr.repository.AnalysisRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AnalysisServiceTest {

  @Mock
  private AnalysisRepository analysisRepository;

  @InjectMocks
  private AnalysisService analysisService;

  @Test
  void shouldThrowExceptionForNotFoundAnalysis() {
    when(analysisRepository.findByAnalysisId(any(UUID.class)))
        .thenReturn(Optional.empty());

    UUID analysisId = UUID.randomUUID();
    assertThatThrownBy(() -> analysisService.retrieveAnalysis(analysisId))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining(analysisId.toString());
  }
}
