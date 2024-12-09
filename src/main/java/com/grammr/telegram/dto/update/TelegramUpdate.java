package com.grammr.telegram.dto.update;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class TelegramUpdate {

  protected final long chatId;
  protected final long updateId;
  protected final String text; // can be empty for InlineKeyboardUpdates

  public abstract boolean hasCallback();
}
