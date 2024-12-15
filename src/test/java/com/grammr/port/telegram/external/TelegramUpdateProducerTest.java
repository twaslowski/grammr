package com.grammr.port.telegram.external;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.grammr.telegram.dto.update.TelegramTextUpdate;
import com.grammr.telegram.dto.update.TelegramUpdate;
import com.grammr.telegram.exception.UpdateNotProcessableException;
import com.grammr.telegram.external.TelegramUpdateProducer;
import com.grammr.telegram.external.factory.TelegramUpdateFactory;
import java.util.Queue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Update;

@ExtendWith(MockitoExtension.class)
class TelegramUpdateProducerTest {

  @InjectMocks
  private TelegramUpdateProducer telegramPoller;

  @Mock
  private Queue<TelegramUpdate> incomingQueue;

  @Mock
  private TelegramUpdateFactory telegramUpdateFactory;

  @Test
  void shouldAddUpdateToIncomingQueue() {
    var telegramUpdate = TelegramTextUpdate.builder()
        .updateId(1)
        .text("some text")
        .chatId(1)
        .build();
    var update = new Update();

    when(telegramUpdateFactory.createTelegramUpdate(update)).thenReturn(telegramUpdate);
    // When update is processed
    telegramPoller.consume(update);

    // Then telegramUpdate object is added to Queue
    verify(incomingQueue).add(TelegramTextUpdate.builder()
        .updateId(1)
        .text("some text")
        .chatId(1)
        .build());
  }

  @Test
  void shouldThrowExceptionIfRequiredFieldMissing() {
    // Given an update with text and a chatId
    var update = new Update();

    update.setUpdateId(1);

    // When extracting the text
    assertThatThrownBy(() -> telegramPoller.consume(update))
        .isInstanceOf(UpdateNotProcessableException.class);
  }
}