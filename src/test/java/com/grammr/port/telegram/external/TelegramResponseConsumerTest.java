package com.grammr.port.telegram.external;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.grammr.port.telegram.dto.response.TelegramResponse;
import com.grammr.port.telegram.dto.response.TelegramTextResponse;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@ExtendWith(MockitoExtension.class)
public class TelegramResponseConsumerTest {

  private final TelegramClient telegramClient = mock(TelegramClient.class);

  private final BlockingQueue<TelegramResponse> outgoingQueue = new LinkedBlockingQueue<>();

  private final TelegramResponseConsumer telegramResponseConsumer = new TelegramResponseConsumer(
      outgoingQueue,
      telegramClient
  );

  @Test
  @SneakyThrows
  void shouldSendMessageFromQueue() {
    // Given
    var response = TelegramTextResponse.builder()
        .chatId(1)
        .text("Hello")
        .build();

    outgoingQueue.add(response);

    // When
    telegramResponseConsumer.handleItem(response);

    // Then
    var captor = ArgumentCaptor.forClass(SendMessage.class);
    verify(telegramClient).execute(captor.capture());

    var sendMessage = captor.getValue();
    assert sendMessage.getChatId().equals("1");
    assert sendMessage.getText().equals("Hello");
  }
}