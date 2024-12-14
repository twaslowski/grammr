package com.grammr.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.grammr.domain.entity.UserSpec;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.event.AnalysisRequestEventSpec;
import com.grammr.domain.value.language.LanguageRecognition;
import com.grammr.domain.value.language.MorphologicalAnalysisSpec;
import com.grammr.service.language.morphology.MorphologicalAnalysisService;
import com.grammr.service.language.recognition.LanguageRecognitionService;
import com.grammr.service.language.translation.literal.LiteralTranslationService;
import com.grammr.service.language.translation.semantic.SemanticTranslationService;
import java.util.List;
import org.junit.jupiter.api.Test;

class FullAnalysisServiceTest {

  private final SemanticTranslationService semanticTranslationService = mock(SemanticTranslationService.class);

  private final LiteralTranslationService literalTranslationService = mock(LiteralTranslationService.class);

  private final LanguageRecognitionService languageRecognitionService = mock(LanguageRecognitionService.class);

  private final MorphologicalAnalysisService morphologicalAnalysisService = mock(MorphologicalAnalysisService.class);

  private final TokenService tokenService = new TokenService();

  private final FullAnalysisService fullAnalysisService = FullAnalysisService.builder()
      .analysisService(morphologicalAnalysisService)
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

    when(languageRecognitionService.recognizeLanguage(phrase)).thenReturn(new LanguageRecognition(LanguageCode.DE));
    when(morphologicalAnalysisService.analyze(phrase, LanguageCode.DE)).thenReturn(MorphologicalAnalysisSpec.valid().build());

    fullAnalysisService.processFullAnalysisRequest(event);

    verify(semanticTranslationService).createSemanticTranslation(phrase, LanguageCode.EN);
    verify(literalTranslationService).translateTokens(phrase, List.of("Hallo", "Welt"));
    verify(morphologicalAnalysisService).analyze(phrase, LanguageCode.DE);
  }

  @Test
  void shouldNotPerformExtensiveAnalysisForLongPhrases() {
    var user = UserSpec.valid().build();
    var phrase = "This is a phrase longer than fifteen analyzedTokens a e i o u and sometimes y too";
    var event = AnalysisRequestEventSpec.valid(user).phrase(phrase).build();

    when(languageRecognitionService.recognizeLanguage(phrase)).thenReturn(LanguageRecognition.of(LanguageCode.DE));

    fullAnalysisService.processFullAnalysisRequest(event);

    verify(semanticTranslationService).createSemanticTranslation(phrase, LanguageCode.EN);
    verify(literalTranslationService, never()).translateTokens(any(), anyList());
    verify(morphologicalAnalysisService, never()).analyze(any(), any());
  }
}