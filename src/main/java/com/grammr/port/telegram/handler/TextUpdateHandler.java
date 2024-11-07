package com.grammr.port.telegram.handler;

import com.grammr.domain.event.AnalysisRequestEvent;
import com.grammr.port.telegram.dto.response.TelegramResponse;
import com.grammr.port.telegram.dto.update.TelegramUpdate;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TextUpdateHandler implements UpdateHandler {

  private final BlockingQueue<AnalysisRequestEvent> analysisRequestQueue;

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    analysisRequestQueue.add(
        AnalysisRequestEvent.builder()
            .phrase(update.getText())
            .chatId(update.getChatId())
            .requestId(UUID.randomUUID().toString())
            .build()
    );
    return null;
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return !update.getText().isEmpty();
  }
}
