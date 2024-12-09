package com.grammr.port.telegram.handler.command;

import com.grammr.port.telegram.dto.update.TelegramUpdate;
import com.grammr.port.telegram.handler.UpdateHandler;

public abstract class AbstractCommandHandler implements UpdateHandler {

  abstract String getCommand();

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return update.getText() != null && update.getText().equals(getCommand());
  }
}
