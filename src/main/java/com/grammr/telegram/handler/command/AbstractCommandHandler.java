package com.grammr.telegram.handler.command;

import com.grammr.telegram.dto.update.TelegramUpdate;
import com.grammr.telegram.handler.UpdateHandler;

public abstract class AbstractCommandHandler implements UpdateHandler {

  abstract String getCommand();

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return update.getText() != null && update.getText().equals(getCommand());
  }
}
