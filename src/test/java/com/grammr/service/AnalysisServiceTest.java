package com.grammr.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.grammr.domain.entity.UserSpec;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.event.AnalysisRequestEventSpec;
import com.grammr.domain.value.AnalysisComponentRequest;
import com.grammr.domain.value.language.LanguageRecognition;
import com.grammr.domain.value.language.LiteralTranslationSpec;
import com.grammr.domain.value.language.MorphologicalAnalysisSpec;
import com.grammr.domain.value.language.SemanticTranslationSpec;
import com.grammr.service.language.morphology.MorphologicalAnalysisService;
import com.grammr.service.language.recognition.LanguageRecognitionService;
import com.grammr.service.language.translation.literal.LiteralTranslationService;
import com.grammr.service.language.translation.semantic.SemanticTranslationService;
import org.junit.jupiter.api.Test;

class AnalysisServiceTest {

  private final SemanticTranslationService semanticTranslationService = mock(SemanticTranslationService.class);

  private final LiteralTranslationService literalTranslationService = mock(LiteralTranslationService.class);

  private final LanguageRecognitionService languageRecognitionService = mock(LanguageRecognitionService.class);

  private final MorphologicalAnalysisService morphologicalAnalysisService = mock(MorphologicalAnalysisService.class);

  private final TokenService tokenService = new TokenService();

  private final AnalysisService analysisService = AnalysisService.builder()
      .morphologicalAnalysisService(morphologicalAnalysisService)
      .languageRecognitionService(languageRecognitionService)
      .literalTranslationService(literalTranslationService)
      .semanticTranslationService(semanticTranslationService)
      .tokenService(tokenService)
      .build();

  @Test
  void shouldCallAllServices() {
    var phrase = "Hallo Welt";
    var user = UserSpec.valid().build();
    var event = AnalysisRequestEventSpec.valid(user).phrase(phrase).build();

    when(languageRecognitionService.createAnalysisComponent(any(AnalysisComponentRequest.class))).thenReturn(new LanguageRecognition(LanguageCode.DE));
    when(morphologicalAnalysisService.createAnalysisComponent(any(AnalysisComponentRequest.class))).thenReturn(MorphologicalAnalysisSpec.valid().build());
    when(literalTranslationService.createAnalysisComponent(any(AnalysisComponentRequest.class))).thenReturn(LiteralTranslationSpec.valid().build());
    when(semanticTranslationService.createAnalysisComponent(any(AnalysisComponentRequest.class))).thenReturn(SemanticTranslationSpec.valid().build());

    analysisService.processFullAnalysisRequest(event);

    verify(semanticTranslationService).createAnalysisComponent(any(AnalysisComponentRequest.class));
    verify(literalTranslationService).createAnalysisComponent(any(AnalysisComponentRequest.class));
    verify(morphologicalAnalysisService).createAnalysisComponent(any(AnalysisComponentRequest.class));
  }

  @Test
  void shouldNotPerformExtensiveAnalysisForLongPhrases() {
    var user = UserSpec.valid().build();
    var phrase = "This is a phrase longer than fifteen analyzedTokens a e i o u and sometimes y too";
    var event = AnalysisRequestEventSpec.valid(user).phrase(phrase).build();
    var analysisComponentRequest = AnalysisComponentRequest.from(event);

    when(languageRecognitionService.createAnalysisComponent(analysisComponentRequest))
        .thenReturn(LanguageRecognition.of(LanguageCode.DE));

    analysisService.processFullAnalysisRequest(event);

    verify(semanticTranslationService).createAnalysisComponent(any(AnalysisComponentRequest.class));
    verify(literalTranslationService, never()).createAnalysisComponent(any(AnalysisComponentRequest.class));
    verify(morphologicalAnalysisService, never()).createAnalysisComponent(any(AnalysisComponentRequest.class));
  }
}