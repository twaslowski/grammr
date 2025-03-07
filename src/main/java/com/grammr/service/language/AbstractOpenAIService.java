package com.grammr.service.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.common.MessageUtil;
import com.grammr.domain.exception.ErroneousOpenAIResponse;
import com.grammr.domain.exception.UnparsableOpenAIResponse;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.ResponseFormat;
import io.github.sashirestela.openai.domain.chat.Chat;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.Synchronized;
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
  @Synchronized
  public <T extends AIGeneratedContent> CompletableFuture<T> openAIChatCompletion(UserMessage userMessage, Class<T> responseType) {
    var request = chatRequest(userMessage);
    return openAIClient.chatCompletions().create(request).thenApplyAsync(response -> {
      String content = response.firstContent();
      var parsed = readSafe(content, responseType);
      enrichWithTokenUsage(parsed, response);
      log.debug("{} produced response from OpenAI: {}", userMessage.getContent(), content);
      return parsed;
    }).exceptionally(e -> {
      log.error("Failed to get response from OpenAI", e);
      throw new ErroneousOpenAIResponse(e);
    });
  }

  private <T extends AIGeneratedContent> T readSafe(String content, Class<T> responseType) {
    try {
      return objectMapper.readValue(content, responseType);
    } catch (Exception e) {
      log.error("Failed to parse response from OpenAI: {}", content, e);
      throw new UnparsableOpenAIResponse(content, e);
    }
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
