package com.grammr.port.telegram.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.grammr.domain.entity.UserSpec;
import com.grammr.domain.event.AnalysisRequestEvent;
import com.grammr.telegram.dto.update.TelegramTextUpdate;
import com.grammr.telegram.handler.TextUpdateHandler;
import com.grammr.telegram.service.UserService;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TextUpdateHandlerTest {

  @Mock
  private BlockingQueue<AnalysisRequestEvent> analysisRequestQueue;

  @Mock
  private UserService userService;

  @InjectMocks
  private TextUpdateHandler textUpdateHandler;

  @Test
  void shouldBeAbleToHandleTextUpdate() {
    var update = TelegramTextUpdate.builder()
        .chatId(1)
        .text("someText")
        .build();
    assertTrue(textUpdateHandler.canHandle(update));
  }

  @Test
  void shouldNotHandleUpdateWithEmptyText() {
    var update = TelegramTextUpdate.builder()
        .chatId(1)
        .text("")
        .build();
    assertFalse(textUpdateHandler.canHandle(update));
  }

  @Test
  void shouldAddAnalysisRequestToQueue() {
    var update = TelegramTextUpdate.builder()
        .chatId(1)
        .text("someText")
        .build();
    var user = UserSpec.valid().build();
    when(userService.findUserByChatId(1)).thenReturn(Optional.of(user));

    var argumentCaptor = ArgumentCaptor.forClass(AnalysisRequestEvent.class);
    textUpdateHandler.handleUpdate(update);
    verify(analysisRequestQueue).add(argumentCaptor.capture());

    var analysisRequest = argumentCaptor.getValue();
    assertThat(analysisRequest.user().getId()).isEqualTo(1);
    assertThat(analysisRequest.phrase()).isEqualTo("someText");
  }
}