package com.grammr.telegram.handler.command;

import com.grammr.common.MessageUtil;
import com.grammr.service.UserService;
import com.grammr.telegram.dto.response.TelegramResponse;
import com.grammr.telegram.dto.response.TelegramTextResponse;
import com.grammr.telegram.dto.update.TelegramUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StartHandler extends AbstractCommandHandler {

  private static final String COMMAND = "/start";

  private final UserService userService;
  private final MessageUtil messageUtil;

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    userService.createUserFromTelegramId(update.getChatId());

    return TelegramTextResponse.builder()
        .chatId(update.getChatId())
        .text(messageUtil.getMessage("command.start.message"))
        .build();
  }

  @Override
  public String getCommand() {
    return COMMAND;
  }
}
