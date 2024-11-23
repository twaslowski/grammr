package com.grammr.port.telegram.handler;

import com.grammr.domain.event.FullAnalysisRequestEvent;
import com.grammr.port.telegram.dto.response.TelegramResponse;
import com.grammr.port.telegram.dto.response.TelegramTextResponse;
import com.grammr.port.telegram.dto.update.TelegramTextUpdate;
import com.grammr.port.telegram.dto.update.TelegramUpdate;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TextUpdateHandler implements UpdateHandler {

  private final BlockingQueue<FullAnalysisRequestEvent> analysisRequestQueue;

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    analysisRequestQueue.add(
        FullAnalysisRequestEvent.builder()
            .phrase(update.getText())
            .chatId(update.getChatId())
            .requestId(UUID.randomUUID().toString())
            .build()
    );
    return TelegramTextResponse.builder()
        .text("Analysis in progress ...")
        .chatId(update.getChatId())
        .build();
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return update instanceof TelegramTextUpdate && !update.getText().isEmpty();
  }
}
