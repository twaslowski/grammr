package com.grammr.telegram.handler;

import com.grammr.telegram.dto.response.TelegramResponse;
import com.grammr.telegram.dto.update.TelegramUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AudioHandler implements UpdateHandler {

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    return null;
  }

  @Override
  public boolean canHandle(TelegramUpdate update) {
    return update.hasAudio();
  }
}
