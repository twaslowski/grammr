package com.grammr.telegram.handler;

import com.grammr.domain.event.AnalysisRequestEvent;
import com.grammr.telegram.dto.response.TelegramResponse;
import com.grammr.telegram.dto.response.TelegramTextResponse;
import com.grammr.telegram.dto.update.TelegramTextUpdate;
import com.grammr.telegram.dto.update.TelegramUpdate;
import com.grammr.telegram.exception.UserNotFoundException;
import com.grammr.telegram.service.UserService;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TextUpdateHandler implements UpdateHandler {

  private final BlockingQueue<AnalysisRequestEvent> analysisRequestQueue;
  private final UserService userService;

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var user = userService.findUserByChatId(update.getChatId())
        .orElseThrow(() -> new UserNotFoundException(update.getChatId()));
    analysisRequestQueue.add(
        AnalysisRequestEvent.builder()
            .phrase(update.getText())
            .user(user)
            .performSemanticTranslation(true)
            .performLiteralTranslation(true)
            .performMorphologicalAnalysis(true)
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
    return update instanceof TelegramTextUpdate
        && !update.getText().isEmpty()
        // And update is not a command
        && !update.getText().startsWith("/");
  }
}
