package com.grammr.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.grammr.domain.event.AnalysisRequestEventSpec;
import com.grammr.domain.value.AnalysisComponentRequest;
import com.grammr.domain.value.language.LiteralTranslationSpec;
import com.grammr.domain.value.language.MorphologicalAnalysisSpec;
import com.grammr.domain.value.language.SemanticTranslationSpec;
import com.grammr.language.service.MorphologyService;
import com.grammr.language.service.v1.AnalysisService;
import com.grammr.language.service.v1.TokenService;
import com.grammr.language.service.v1.translation.literal.OpenAILiteralTranslationService;
import com.grammr.language.service.v1.translation.semantic.SemanticTranslationService;
import org.junit.jupiter.api.Test;

class AnalysisServiceTest {

  private final SemanticTranslationService semanticTranslationService = mock(SemanticTranslationService.class);

  private final OpenAILiteralTranslationService literalTranslationService = mock(OpenAILiteralTranslationService.class);

  private final MorphologyService morphologyService = mock(MorphologyService.class);

  private final TokenService tokenService = new TokenService();

  private final AnalysisService analysisService = AnalysisService.builder()
      .morphologyService(morphologyService)
      .literalTranslationService(literalTranslationService)
      .semanticTranslationService(semanticTranslationService)
      .tokenService(tokenService)
      .build();

  @Test
  void shouldCallAllServices() {
    var phrase = "Hallo Welt";
    var event = AnalysisRequestEventSpec.valid().phrase(phrase).build();

    when(morphologyService.createAnalysisComponent(any(AnalysisComponentRequest.class))).thenReturn(MorphologicalAnalysisSpec.valid().build());
    when(literalTranslationService.createAnalysisComponent(any(AnalysisComponentRequest.class))).thenReturn(LiteralTranslationSpec.valid().build());
    when(semanticTranslationService.createAnalysisComponent(any(AnalysisComponentRequest.class))).thenReturn(SemanticTranslationSpec.valid().build());

    analysisService.translateAndAnalyze(event);

    verify(semanticTranslationService).createAnalysisComponent(any(AnalysisComponentRequest.class));
    verify(literalTranslationService).createAnalysisComponent(any(AnalysisComponentRequest.class));
    verify(morphologyService).createAnalysisComponent(any(AnalysisComponentRequest.class));
  }
}