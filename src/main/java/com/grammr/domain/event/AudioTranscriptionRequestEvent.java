package com.grammr.domain.event;

import com.grammr.telegram.dto.update.TelegramAudioUpdate;
import java.nio.file.Path;

public record AudioTranscriptionRequestEvent(
    Path path,
    long chatId
) {

  public static AudioTranscriptionRequestEvent from(TelegramAudioUpdate audioUpdate) {
    return new AudioTranscriptionRequestEvent(audioUpdate.getFilePath(), audioUpdate.getChatId());
  }
}
