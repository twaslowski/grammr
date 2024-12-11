package com.grammr.telegram.dto.update;

import com.grammr.domain.entity.User;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class TelegramUpdate {

  protected final long chatId;
  protected final long updateId;
  protected final String text; // can be empty for InlineKeyboardUpdates

  @Setter
  protected User user;

  public abstract boolean hasCallback();

  public abstract boolean hasAudio();
}
