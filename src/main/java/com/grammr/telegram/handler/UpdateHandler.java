package com.grammr.telegram.handler;

import com.grammr.telegram.dto.response.TelegramResponse;
import com.grammr.telegram.dto.update.TelegramUpdate;

public interface UpdateHandler {

  TelegramResponse handleUpdate(TelegramUpdate update);

  boolean canHandle(TelegramUpdate update);
}
