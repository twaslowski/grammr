package com.grammr.port.telegram.handler;

import com.grammr.port.telegram.dto.response.TelegramResponse;
import com.grammr.port.telegram.dto.update.TelegramUpdate;

public interface UpdateHandler {

  TelegramResponse handleUpdate(TelegramUpdate update);

  boolean canHandle(TelegramUpdate update);
}
