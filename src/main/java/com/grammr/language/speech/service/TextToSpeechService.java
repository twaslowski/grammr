package com.grammr.language.speech.service;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.audio.AudioFormat;
import io.github.sashirestela.openai.common.audio.Voice;
import io.github.sashirestela.openai.domain.audio.SpeechRequest;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Deprecated
@RequiredArgsConstructor
public class TextToSpeechService {

  // Todo: Needs to be replaced with a service that supports streaming to reduce time to first byte.
  // Arguably can be performed from the frontend without having to pass through the backend.

  private final SimpleOpenAI client;

  @Value("${openai.tts.model}")
  private String model;

  @Value("${openai.tts.voice}")
  private String voice;

  public CompletableFuture<InputStream> convert(String text, double speed) {
    log.info("Converting text to speech: {}", text);
    var speechRequest = SpeechRequest.builder()
        .input(text)
        .model(model)
        .voice(Voice.NOVA)
        .speed(speed)
        .responseFormat(AudioFormat.MP3).build();
    return client.audios().speak(speechRequest);
  }
}
