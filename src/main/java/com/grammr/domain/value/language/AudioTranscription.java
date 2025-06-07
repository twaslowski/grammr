package com.grammr.domain.value.language;

import com.grammr.common.AIGeneratedContent;
import io.github.sashirestela.openai.domain.audio.Transcription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AudioTranscription extends AIGeneratedContent {

  private String transcription;

  public static AudioTranscription from(Transcription transcription) {
    return AudioTranscription.builder()
        .transcription(transcription.getText())
        .build();
  }
}
