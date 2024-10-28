package com.grammr.port.telegram.dto.update;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Value
@SuperBuilder
public class TelegramTextUpdate extends TelegramUpdate {

  @Override
  public boolean hasCallback() {
    return false;
  }
}
