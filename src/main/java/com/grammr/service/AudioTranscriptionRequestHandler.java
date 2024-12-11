package com.grammr.service;

import com.grammr.domain.event.AnalysisRequestEvent;
import com.grammr.domain.event.AudioTranscriptionRequestEvent;
import com.grammr.service.language.transcription.OpenAITranscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AudioTranscriptionRequestHandler {

  private final ApplicationEventPublisher eventPublisher;
  private final OpenAITranscriptionService transcriptionService;

  @Async
  @EventListener
  public void handleAudioTranscriptionRequest(AudioTranscriptionRequestEvent event) {
    log.info("Received audio transcription request for chatId: {}", event.requestId());
    var transcription = transcriptionService.createAudioTranscription(event.path());
    AnalysisRequestEvent request = AnalysisRequestEvent.full()
        .phrase(transcription.getTranscription())
        .user(event.user())
        .requestId(event.requestId()).build();
    eventPublisher.publishEvent(request);
  }
}