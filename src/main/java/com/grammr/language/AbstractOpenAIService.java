package com.grammr.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.ResponseFormat;
import io.github.sashirestela.openai.domain.chat.Chat;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import lombok.SneakyThrows;

public abstract class AbstractOpenAIService<T> {

  protected ObjectMapper objectMapper;
  protected SimpleOpenAI openAIClient;
  protected String modelName;

  @SneakyThrows
  public T openAIChatCompletion(String phrase, Class<T> responseType) {
    var request = chatRequest(phrase);
    var futureChat = openAIClient.chatCompletions().create(request);
    Chat response = futureChat.join();
    return objectMapper.readValue(response.firstContent(), responseType);
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
