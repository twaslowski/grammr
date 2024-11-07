package com.grammr.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.ResponseFormat;
import io.github.sashirestela.openai.domain.chat.Chat;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public abstract class AbstractOpenAIService {

  protected ObjectMapper objectMapper;
  protected SimpleOpenAI openAIClient;

  @Getter
  protected String modelName;

  @SneakyThrows
  public <T> T openAIChatCompletion(String phrase, Class<T> responseType) {
    var request = chatRequest(phrase);
    var futureChat = openAIClient.chatCompletions().create(request);
    Chat response = futureChat.join();
    String content = response.firstContent();
    log.info("Got response from OpenAI: {}; tokens: {}", content, response.getUsage().getTotalTokens());
    return objectMapper.readValue(content, responseType);
  }

  private ChatRequest chatRequest(String phrase) {
    return ChatRequest.builder()
        .model(modelName)
        .message(getSystemMessage())
        .message(generateUserMessage(phrase))
        .responseFormat(getResponseFormat())
        .maxCompletionTokens(100)
        .build();
  }

  protected abstract UserMessage generateUserMessage(String param);

  protected abstract SystemMessage getSystemMessage();

  protected abstract ResponseFormat getResponseFormat();
}
