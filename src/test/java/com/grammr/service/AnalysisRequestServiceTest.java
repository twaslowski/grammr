package com.grammr.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.grammr.domain.event.AnalysisRequestEventSpec;
import com.grammr.domain.value.language.MorphologicalAnalysisSpec;
import com.grammr.service.language.morphology.MorphologicalAnalysisService;
import com.grammr.service.language.translation.literal.LiteralTranslationService;
import com.grammr.service.language.translation.semantic.SemanticTranslationService;
import java.util.List;
import org.junit.jupiter.api.Test;

class AnalysisRequestServiceTest {

  private final SemanticTranslationService semanticTranslationService = mock(SemanticTranslationService.class);

  private final LiteralTranslationService literalTranslationService = mock(LiteralTranslationService.class);

  private final MorphologicalAnalysisService morphologicalAnalysisService = mock(MorphologicalAnalysisService.class);

  private final TokenService tokenService = new TokenService();

  private final AnalysisRequestService analysisRequestService = new AnalysisRequestService(
      semanticTranslationService, literalTranslationService, morphologicalAnalysisService, tokenService
  );

  @Test
  void shouldCallAllServices() {
    var phrase = "Hallo Welt";
    var event = AnalysisRequestEventSpec.valid().phrase(phrase).build();
    when(morphologicalAnalysisService.analyze(phrase)).thenReturn(MorphologicalAnalysisSpec.valid().build());

    analysisRequestService.processFullAnalysisRequest(event);

    verify(semanticTranslationService).createSemanticTranslation(phrase);
    verify(literalTranslationService).translateTokens(phrase, List.of("Hallo", "Welt"));
    verify(morphologicalAnalysisService).analyze(phrase);
  }

  @Test
  void shouldNotPerformExtensiveAnalysisForLongPhrases() {
    var phrase = "This is a phrase longer than fifteen tokens a e i o u and sometimes y too";
    var event = AnalysisRequestEventSpec.valid().phrase(phrase).build();

    analysisRequestService.processFullAnalysisRequest(event);

    verify(semanticTranslationService).createSemanticTranslation(phrase);
    verify(literalTranslationService, never()).translateTokens(any(), anyList());
    verify(morphologicalAnalysisService, never()).analyze(phrase);
  }
}