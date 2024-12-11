package com.grammr.telegram.handler;

import com.grammr.service.UserService;
import com.grammr.telegram.dto.response.TelegramResponse;
import com.grammr.telegram.dto.response.TelegramTextResponse;
import com.grammr.telegram.dto.update.TelegramTextUpdate;
import com.grammr.telegram.dto.update.TelegramUpdate;
import com.grammr.telegram.exception.UserNotFoundException;
import com.grammr.telegram.service.AnalysisInitiationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TextUpdateHandler implements UpdateHandler {

  private final UserService userService;
  private final AnalysisInitiationService analysisInitiationService;

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var user = userService.findUserByChatId(update.getChatId())
        .orElseThrow(() -> new UserNotFoundException(update.getChatId()));
    var phrase = update.getText();
    analysisInitiationService.initiateAnalysis(phrase, user);
    return TelegramTextResponse.builder()
        .text("Analysis in progress ...")
        .chatId(update.getChatId())
        .build();
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return update instanceof TelegramTextUpdate
        && !update.getText().isEmpty()
        && !update.getText().startsWith("/");
  }
}
