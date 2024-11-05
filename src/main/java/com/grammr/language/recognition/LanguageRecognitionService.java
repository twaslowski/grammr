package com.grammr.language.recognition;

import static java.lang.String.format;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.LanguageRecognition;
import com.grammr.llm.OpenAIService;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class LanguageRecognitionService {

  private final OpenAIService openAIService;
  private final ObjectMapper objectMapper;

  public LanguageRecognition recognizeLanguage(String phrase) {
    return openAIService.performRequest(
        ChatRequest.builder()
            .message(getSystemMessage())
            .message(generateUserMessage(phrase))
            .model("gpt-4o-mini")
            .maxCompletionTokens(100)
            .build(),
        LanguageRecognition.class
    );
  }

  @SneakyThrows
  public SystemMessage getSystemMessage() {
    return SystemMessage.of(format(
        "You identify languages of phrases. Return a JSON with the following structure: %s",
        objectMapper.writeValueAsString(new LanguageRecognition(LanguageCode.DE))));
  }

  public UserMessage generateUserMessage(String phrase) {
    return UserMessage.of(format("Identify the language of the following phrase: %s", phrase));
  }
}
