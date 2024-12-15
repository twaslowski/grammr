package com.grammr.integration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.entity.UserSpec;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.event.AnalysisRequestEvent;
import com.grammr.domain.value.language.Token;
import com.grammr.domain.value.language.TokenTranslation;
import com.grammr.telegram.dto.update.TelegramTextUpdate;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@IntegrationTest
public class FullAnalysisIntegrationTest extends IntegrationTestBase {

  @Test
  @SneakyThrows
  void shouldPerformCompleteAnalysis() {
    var sourcePhrase = "Ich lerne heute Deutsch";
    var phraseTranslation = "I am learning German today";
    var tokens = tokenService.tokenize(sourcePhrase);
    var words = tokens.stream().map(Token::text).toList();

    var user = userRepository.save(UserSpec.valid().build());

    mockLanguageRecognition(sourcePhrase, user.getLanguageLearned());
    mockSemanticTranslation(sourcePhrase, phraseTranslation, user.getLanguageSpoken());
    for (String word : words) {
      mockTokenTranslation(sourcePhrase, word, new TokenTranslation(word, "someTranslation"));
    }

    // given a text update
    incomingMessageQueue.add(TelegramTextUpdate.builder()
        .text(sourcePhrase)
        .chatId(user.getChatId())
        .build());

    await().atMost(5, SECONDS).untilAsserted(() -> {
      assertThat(requestRepository.count()).isEqualTo(1);

      assertThat(requestRepository.findAll().getFirst()).isNotNull();

      assertThat(outgoingMessageQueue).isNotEmpty();
      var analysisCompleteEvents = eventAccumulator.getAnalysisCompleteEvents();
      assertThat(analysisCompleteEvents).isNotEmpty();

      var analysis = analysisCompleteEvents.getFirst().fullAnalysis();
      assertThat(analysis).isNotNull();
      assertThat(analysis.semanticTranslation().getSourcePhrase()).isEqualTo(sourcePhrase);
      assertThat(analysis.semanticTranslation().getTranslatedPhrase()).isEqualTo(phraseTranslation);
      assertThat(analysis.analyzedTokens()).allMatch(token -> token.morphology() != null);
    });
  }

  @Test
  @SneakyThrows
  void shouldPerformCompleteAnalysisWhenReceivingPhraseInUserSpokenLanguage() {
    var user = userRepository.save(UserSpec.valid()
        .languageLearned(LanguageCode.DE)
        .languageSpoken(LanguageCode.EN)
        .build());

    var sourcePhrase = "I am learning German today";
    var translation = "Ich lerne heute Deutsch";

    var tokens = tokenService.tokenize(translation);
    var words = tokens.stream().map(Token::text).toList();

    mockLanguageRecognition(sourcePhrase, LanguageCode.EN);
    mockSemanticTranslation(sourcePhrase, translation, LanguageCode.DE);
    for (String word : words) {
      mockTokenTranslation(translation, word, new TokenTranslation(word, "someTranslation"));
    }

    incomingMessageQueue.add(TelegramTextUpdate.builder()
        .text(sourcePhrase)
        .chatId(user.getChatId())
        .build());

    await().atMost(5, SECONDS).untilAsserted(() -> {
      assertThat(requestRepository.count()).isEqualTo(1);

      assertThat(requestRepository.findAll().getFirst()).isNotNull();

      assertThat(outgoingMessageQueue).isNotEmpty();
      var analysisCompleteEvents = eventAccumulator.getAnalysisCompleteEvents();
      assertThat(analysisCompleteEvents).isNotEmpty();

      var analysis = analysisCompleteEvents.getFirst().fullAnalysis();
      assertThat(analysis).isNotNull();
      assertThat(analysis.semanticTranslation().getSourcePhrase()).isEqualTo(sourcePhrase);
      assertThat(analysis.semanticTranslation().getTranslatedPhrase()).isEqualTo(translation);
      assertThat(analysis.analyzedTokens()).allMatch(token -> token.morphology() != null);
    });
  }

  @Test
  @Disabled("Will build feature to default to translations to English in the future")
  void shouldOnlyReturnSemanticTranslationIfUnknownLanguageCode() {
    var sourcePhrase = "Donde esta la biblioteca?";

    var user = userRepository.save(UserSpec.valid().build());

    // given
    var analysisRequest = AnalysisRequestEvent.builder()
        .phrase(sourcePhrase)
        .build();

    mockLanguageRecognition(sourcePhrase, LanguageCode.UNSUPPORTED);
    mockSemanticTranslation(sourcePhrase, "Where is the library?", LanguageCode.EN);

    // when
    var fullAnalysis = fullAnalysisService.processFullAnalysisRequest(analysisRequest);

    // then
    assertThat(fullAnalysis).isNotNull();
    assertThat(fullAnalysis.analyzedTokens()).isEmpty();
    assertThat(fullAnalysis.semanticTranslation()).isNotNull();
    assertThat(fullAnalysis.semanticTranslation().getTranslatedPhrase()).isEqualTo("Where is the library?");
  }
}
