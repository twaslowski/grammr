package com.grammr.telegram.handler;

import com.grammr.common.MessageUtil;
import com.grammr.domain.entity.Request;
import com.grammr.domain.entity.Request.Status;
import com.grammr.domain.event.AudioTranscriptionRequestEvent;
import com.grammr.repository.RequestRepository;
import com.grammr.service.UserService;
import com.grammr.telegram.dto.response.TelegramResponse;
import com.grammr.telegram.dto.response.TelegramTextResponse;
import com.grammr.telegram.dto.update.TelegramAudioUpdate;
import com.grammr.telegram.dto.update.TelegramUpdate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AudioHandler implements UpdateHandler {

  private final ApplicationEventPublisher applicationEventPublisher;
  private final UserService userService;
  private final RequestRepository requestRepository;
  private final MessageUtil messageUtil;

  @Override
  public TelegramResponse handleUpdate(TelegramUpdate update) {
    var audioUpdate = (TelegramAudioUpdate) update;
    var user = userService.findUserByChatId(audioUpdate.getChatId());

    var request = requestRepository.save(Request.builder()
        .requestId(UUID.randomUUID().toString())
        .chatId(audioUpdate.getChatId())
        .status(Status.PENDING)
        .build());

    var audioTranscriptionRequestEvent = AudioTranscriptionRequestEvent.builder()
        .path(audioUpdate.getFilePath())
        .userLanguageSpoken(user.getLanguageSpoken())
        .userLanguageLearned(user.getLanguageLearned())
        .requestId(request.getRequestId())
        .build();

    applicationEventPublisher.publishEvent(audioTranscriptionRequestEvent);
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
