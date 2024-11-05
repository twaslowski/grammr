package com.grammr.llm;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.chat.Chat;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenAIService {

  private final SimpleOpenAI openAIClient;
  private final ObjectMapper objectMapper;

  @SneakyThrows
  public <T> T performRequest(ChatRequest chatRequest, Class<T> responseType) {
    var futureChat = openAIClient.chatCompletions().create(chatRequest);
    Chat response = futureChat.join();
    return objectMapper.readValue(response.firstContent(), responseType);
  }
}
