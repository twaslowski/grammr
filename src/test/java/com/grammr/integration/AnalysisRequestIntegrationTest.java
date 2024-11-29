package com.grammr.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.event.AnalysisRequestEvent;
import com.grammr.domain.value.language.SemanticTranslation;
import com.grammr.domain.value.language.Token;
import com.grammr.domain.value.language.TokenTranslation;
import com.grammr.service.AnalysisRequestService;
import com.grammr.service.TokenService;
import com.grammr.service.language.translation.literal.LiteralTranslationService;
import com.grammr.service.language.translation.semantic.SemanticTranslationService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@IntegrationTest
public class AnalysisRequestIntegrationTest {

  @MockBean
  private SemanticTranslationService semanticTranslationService;

  @MockBean
  private LiteralTranslationService literalTranslationService;

  @Autowired
  private AnalysisRequestService analysisRequestService;

  @Autowired
  private TokenService tokenService;

  @Test
  void shouldPerformCompleteAnalysis() {
    var sourcePhrase = "Ich lerne heute Deutsch.";
    var tokens = tokenService.tokenize(sourcePhrase);
    var words = tokens.stream().map(Token::text).toList();
    // given
    var analysisRequest = AnalysisRequestEvent.builder()
        .phrase(sourcePhrase)
        .requestId("123")
        .chatId(1)
        .build();

    when(semanticTranslationService.createSemanticTranslation(analysisRequest.phrase()))
        .thenReturn(SemanticTranslation.builder()
            .sourcePhrase(sourcePhrase)
            .translatedPhrase("I am learning German today.")
            .build());

    when(literalTranslationService.translateTokens(sourcePhrase, words))
        .thenReturn(List.of(new TokenTranslation("I", "Ich"),
            new TokenTranslation("learn", "lerne"),
            new TokenTranslation("today", "heute"),
            new TokenTranslation("German", "Deutsch"))
        );

    // when
    var fullAnalysis = analysisRequestService.processFullAnalysisRequest(analysisRequest);

    // then
    assertThat(fullAnalysis).isNotNull();
    assertThat(fullAnalysis.tokens()).allMatch(token -> token.morphology() != null);
  }
}
