package com.grammr.service.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.common.MessageUtil;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.ResponseFormat;
import io.github.sashirestela.openai.domain.chat.Chat;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public abstract class AbstractOpenAIService {

  protected ObjectMapper objectMapper;
  protected SimpleOpenAI openAIClient;
  protected MessageUtil messageUtil;
  protected String modelName;

  @SneakyThrows
  public <T extends AIGeneratedContent> T openAIChatCompletion(UserMessage userMessage, Class<T> responseType) {
    var request = chatRequest(userMessage);
    var futureChat = openAIClient.chatCompletions().create(request);
    Chat response = futureChat.join();
    String content = response.firstContent();
    var parsed = objectMapper.readValue(content, responseType);
    enrichWithTokenUsage(parsed, response);
    log.info("Got response from OpenAI: {}; analyzedTokens: {}", content, response.getUsage().getTotalTokens());
    return parsed;
  }

  private <T extends AIGeneratedContent> void enrichWithTokenUsage(T parsed, Chat response) {
    parsed.setCompletionTokens(response.getUsage().getCompletionTokens());
    parsed.setPromptTokens(response.getUsage().getPromptTokens());
  }

  private ChatRequest chatRequest(UserMessage userMessage) {
    return ChatRequest.builder()
        .model(modelName)
        .message(getSystemMessage())
        .message(userMessage)
        .responseFormat(getResponseFormat())
        .maxCompletionTokens(100)
        .build();
  }

  protected abstract SystemMessage getSystemMessage();

  protected abstract ResponseFormat getResponseFormat();
}
