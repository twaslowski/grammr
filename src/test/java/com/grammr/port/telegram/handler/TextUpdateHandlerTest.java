package com.grammr.port.telegram.handler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.grammr.domain.entity.UserSpec;
import com.grammr.service.UserService;
import com.grammr.telegram.dto.update.TelegramTextUpdate;
import com.grammr.telegram.handler.TextUpdateHandler;
import com.grammr.telegram.service.AnalysisInitiationService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TextUpdateHandlerTest {

  @Mock
  private UserService userService;

  @Mock
  private AnalysisInitiationService analysisInitiationService;

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

    textUpdateHandler.handleUpdate(update);
    verify(analysisInitiationService).initiateAnalysis(update.getText(), user);
  }
}