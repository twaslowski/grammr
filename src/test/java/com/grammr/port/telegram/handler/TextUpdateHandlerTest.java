package com.grammr.port.telegram.handler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.grammr.domain.event.FullAnalysisRequest;
import com.grammr.port.telegram.dto.update.TelegramTextUpdate;
import java.util.concurrent.BlockingQueue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TextUpdateHandlerTest {

  @Mock
  private BlockingQueue<FullAnalysisRequest> analysisRequestQueue;

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
    textUpdateHandler.handleUpdate(update);
    verify(analysisRequestQueue).add(any(FullAnalysisRequest.class));
  }
}