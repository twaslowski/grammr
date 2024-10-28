package com.grammr.port.telegram.handler.command;

import com.grammr.port.telegram.MessageUtil;
import com.grammr.port.telegram.dto.update.TelegramUpdate;
import com.grammr.port.telegram.handler.UpdateHandler;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractCommandHandler implements UpdateHandler {

  protected final String command;
  protected final MessageUtil messageUtil;

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return update.getText() != null && update.getText().equals(command);
  }
}
