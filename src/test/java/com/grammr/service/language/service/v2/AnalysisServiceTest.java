package com.grammr.service.language.service.v2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.grammr.domain.entity.Analysis;
import com.grammr.domain.exception.BadRequestException;
import com.grammr.domain.exception.ResourceNotFoundException;
import com.grammr.domain.value.language.Token;
import com.grammr.domain.value.language.v2.WordTranslation;
import com.grammr.language.service.v2.analysis.AnalysisService;
import com.grammr.repository.AnalysisRepository;
import java.util.List;
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

  @Test
  void shouldUpdateAnalysisWithValidToken() {
    UUID analysisId = UUID.randomUUID();
    var translation = WordTranslation.builder().build();
    Token token = new Token(0, "some-word", translation, null);

    Analysis analysis = Analysis.builder()
        .analysisId(analysisId)
        .analysedTokens(List.of(new Token(0, "some-word", null, null)))
        .build();

    when(analysisRepository.findByAnalysisId(analysisId)).thenReturn(Optional.of(analysis));
    when(analysisRepository.save(any(Analysis.class))).thenReturn(analysis);

    analysisService.updateAnalysis(analysisId, token);

    assertThat(analysis.getAnalysedTokens().getFirst().translation()).isEqualTo(translation);
  }

  @Test
  void shouldThrowExceptionWhenUpdatingNonExistentAnalysis() {
    UUID analysisId = UUID.randomUUID();
    Token token = new Token(0, "word", null, null);

    when(analysisRepository.findByAnalysisId(analysisId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> analysisService.updateAnalysis(analysisId, token))
        .isInstanceOf(BadRequestException.class)
        .hasMessageContaining(analysisId.toString());
  }
}
