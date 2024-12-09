package com.grammr.port.telegram;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.grammr.telegram.TelegramUpdateDelegator;
import com.grammr.telegram.dto.update.TelegramTextUpdate;
import com.grammr.telegram.handler.TextUpdateHandler;
import java.util.List;
import org.junit.jupiter.api.Test;

class TelegramUpdateDelegatorTest {

  private final TextUpdateHandler textUpdateHandler = mock(TextUpdateHandler.class);

  private final TelegramUpdateDelegator telegramUpdateDelegator =
      new TelegramUpdateDelegator(List.of(textUpdateHandler));

  @Test
  void shouldDelegateTextUpdateToTextHandler() {
    // given
    var update = TelegramTextUpdate.builder()
        .text("Hello, world!")
        .chatId(1)
        .build();

    when(textUpdateHandler.canHandle(update)).thenReturn(true);

    // when
    telegramUpdateDelegator.delegateUpdate(update);

    // then
    verify(textUpdateHandler).handleUpdate(update);
  }
}