package com.grammr.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.grammr.annotation.IntegrationTest;
import com.grammr.telegram.dto.update.TelegramTextUpdate;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@IntegrationTest
public class UserIntegrationTest extends IntegrationTestBase {

  @Test
  void shouldCreateUserOnStartCommandIfNotExists() {
    var update = TelegramTextUpdate.builder()
        .chatId(1L)
        .text("/start")
        .build();

    incomingMessageQueue.add(update);

    await().atMost(3, TimeUnit.SECONDS)
        .untilAsserted(() -> {
          var user = userRepository.findByChatId(1L).orElseThrow();

          assertThat(outgoingMessageQueue).hasSize(1);
          var message = outgoingMessageQueue.remove();
          assertThat(message.getChatId()).isEqualTo(1L);
          assertThat(message.getText()).isEqualTo(
              messageUtil.parameterizeMessage("command.start.message",
                  user.getLanguageSpoken().getLanguageName(), user.getLanguageLearned().getLanguageName()));
        });
  }

  @Test
  void shouldReturnErrorMessageIfUserDoesNotExist() {
    var update = TelegramTextUpdate.builder()
        .chatId(1L)
        .text("Hello")
        .build();

    incomingMessageQueue.add(update);

    await().atMost(3, TimeUnit.SECONDS)
        .untilAsserted(() -> {
          assertThat(outgoingMessageQueue).hasSize(1);
          var message = outgoingMessageQueue.remove();
          assertThat(message.getChatId()).isEqualTo(1L);
          assertThat(message.getText()).isEqualTo(messageUtil.getMessage("telegram.error.user.not-found"));
        });
  }
}