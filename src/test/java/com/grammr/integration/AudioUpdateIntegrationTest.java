package com.grammr.integration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.entity.Request.Status;
import com.grammr.domain.entity.UserSpec;
import com.grammr.domain.value.language.TokenTranslation;
import com.grammr.telegram.dto.update.TelegramAudioUpdate;
import java.nio.file.Path;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

@IntegrationTest
public class AudioUpdateIntegrationTest extends IntegrationTestBase {

  @Test
  @SneakyThrows
  void shouldPerformTranscriptionAndAnalysis() {
    // given an audio update
    var user = userRepository.save(UserSpec.valid().build());

    incomingMessageQueue.add(TelegramAudioUpdate.builder()
        .filePath(Path.of("someFile"))
        .chatId(user.getChatId())
        .build());

    var transcriptionOutput = "Ich lerne Deutsch";
    var translation = "I am learning German";
    var words = List.of("Ich", "lerne", "Deutsch");

    mockAudioTranscription(transcriptionOutput);
    mockLanguageRecognition(transcriptionOutput, user.getLanguageLearned());
    mockSemanticTranslation(transcriptionOutput, translation, user.getLanguageSpoken());
    for (String word : words) {
      mockTokenTranslation(transcriptionOutput, word, new TokenTranslation(word, "someTranslation"));
    }

    await().atMost(5, SECONDS).untilAsserted(() -> {
      assertThat(requestRepository.count()).isEqualTo(1);

      var request = requestRepository.findAll().getFirst();
      assertThat(request.getCompletionTokens()).isNotZero();
      assertThat(request.getPromptTokens()).isNotZero();
      assertThat(request.getStatus()).isEqualTo(Status.COMPLETED);

      assertThat(outgoingMessageQueue).isNotEmpty();
      var analysisCompleteEvents = eventAccumulator.getAnalysisCompleteEvents();
      assertThat(analysisCompleteEvents).isNotEmpty();

      var analysis = analysisCompleteEvents.getFirst().fullAnalysis();
      assertThat(analysis).isNotNull();
      assertThat(analysis.semanticTranslation().getSourcePhrase()).isEqualTo(transcriptionOutput);
      assertThat(analysis.semanticTranslation().getTranslatedPhrase()).isEqualTo(translation);
      assertThat(analysis.analyzedTokens()).allMatch(token -> token.morphology() != null);
    });
  }
}
