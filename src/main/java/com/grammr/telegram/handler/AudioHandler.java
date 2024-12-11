package com.grammr.telegram.handler;

import com.grammr.common.MessageUtil;
import com.grammr.domain.event.AudioTranscriptionRequestEvent;
import com.grammr.telegram.dto.response.TelegramResponse;
import com.grammr.telegram.dto.response.TelegramTextResponse;
import com.grammr.telegram.dto.update.TelegramAudioUpdate;
import com.grammr.telegram.dto.update.TelegramUpdate;
import java.util.concurrent.BlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AudioHandler implements UpdateHandler {

  private final BlockingQueue<AudioTranscriptionRequestEvent> transcriptionRequestQueue;
  private final MessageUtil messageUtil;

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var audioUpdate = (TelegramAudioUpdate) update;
    var audioFile = AudioTranscriptionRequestEvent.from(audioUpdate);
    transcriptionRequestQueue.add(audioFile);
    return TelegramTextResponse.builder()
        .chatId(audioUpdate.getChatId())
        .text(messageUtil.getMessage("openai.transcription.submitted"))
        .build();
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return update.hasAudio();
  }
}
