package com.grammr.language.speech.service;

import com.grammr.domain.value.language.AudioTranscription;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.audio.AudioResponseFormat;
import io.github.sashirestela.openai.domain.audio.TranscriptionRequest;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenAITranscriptionService {

  private final SimpleOpenAI openAIClient;

  @Value("${openai.transcription.model}")
  private String modelId;

  @Value("${openai.transcription.verbose}")
  private boolean verboseJson;

  public AudioTranscription createAudioTranscription(Path audioFile) {
    var transcriptionRequest = TranscriptionRequest.builder()
        .file(audioFile)
        .model(modelId)
        .responseFormat(verboseJson ? AudioResponseFormat.VERBOSE_JSON : AudioResponseFormat.JSON)
        .build();
    var transcription = openAIClient.audios().transcribe(transcriptionRequest);
    return AudioTranscription.from(transcription.join());
  }
}
