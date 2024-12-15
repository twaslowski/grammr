package com.grammr.telegram.dto.update;

import java.nio.file.Path;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@Value
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class TelegramAudioUpdate extends TelegramUpdate {

  Path filePath;

  @Override
  public boolean hasCallback() {
    return false;
  }

  @Override
  public boolean hasAudio() {
    return true;
  }
}
