package com.grammr.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.event.AnalysisRequestEvent;
import com.grammr.domain.value.language.Token;
import com.grammr.domain.value.language.TokenTranslation;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

@IntegrationTest
public class FullAnalysisIntegrationTest extends IntegrationTestBase {

  @Test
  @SneakyThrows
  void shouldPerformCompleteAnalysis() {
    var sourcePhrase = "Ich lerne heute Deutsch.";
    var phraseTranslation = "I am learning German today";
    var tokens = tokenService.tokenize(sourcePhrase);
    var words = tokens.stream().map(Token::text).toList();
    // given
    var analysisRequest = AnalysisRequestEvent.builder()
        .phrase(sourcePhrase)
        .requestId("123")
        .build();

    mockLanguageRecognition(sourcePhrase, LanguageCode.DE);
    mockSemanticTranslation(sourcePhrase, phraseTranslation);
    for (String word : words) {
      mockTokenTranslation(sourcePhrase, word, new TokenTranslation(word, "someTranslation"));
    }

    // when
    var fullAnalysis = analysisRequestService.processFullAnalysisRequest(analysisRequest);

    // then
    assertThat(fullAnalysis).isNotNull();
    assertThat(fullAnalysis.semanticTranslation().getTranslatedPhrase()).isEqualTo(phraseTranslation);
    assertThat(fullAnalysis.analyzedTokens()).allMatch(token -> token.morphology() != null);
  }

  @Test
  void shouldOnlyReturnSemanticTranslationIfUnknownLanguageCode() {
    var sourcePhrase = "Donde esta la biblioteca?";

    // given
    var analysisRequest = AnalysisRequestEvent.builder()
        .phrase(sourcePhrase)
        .build();

    mockLanguageRecognition(sourcePhrase, LanguageCode.UNSUPPORTED);
    mockSemanticTranslation(sourcePhrase, "Where is the library?");

    // when
    var fullAnalysis = analysisRequestService.processFullAnalysisRequest(analysisRequest);

    // then
    assertThat(fullAnalysis).isNotNull();
    assertThat(fullAnalysis.analyzedTokens()).isEmpty();
    assertThat(fullAnalysis.semanticTranslation()).isNotNull();
  }
}
