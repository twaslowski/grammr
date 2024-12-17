package com.grammr.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.sashirestela.openai.domain.audio.Transcription;
import io.github.sashirestela.openai.domain.chat.Chat;
import io.github.sashirestela.openai.domain.chat.ChatMessage.ChatRole;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.mockito.ArgumentMatcher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenAITestUtil {

  public static final String MOCK_FILE_PATH = "src/test/resources/rest/openai_sample_response.json";

  private final ObjectMapper objectMapper;

  public static ArgumentMatcher<ChatRequest> chatRequestEquals(String prompt) {
    return request -> request != null
        && getUserMessage(request).getContent().toString().equals(prompt);
  }

  public static ArgumentMatcher<ChatRequest> chatRequestContains(String partialPrompt) {
    return request -> request != null
        && getUserMessage(request).getContent().toString().contains(partialPrompt);
  }

  private static UserMessage getUserMessage(ChatRequest request) {
    return (UserMessage) request.getMessages().stream()
        .filter(message -> message.getRole().equals(ChatRole.USER))
        .findFirst()
        .orElseThrow();
  }

  @SneakyThrows
  public Chat parameterizeChatResponse(String responseContent) {
    JsonNode mockResponseNode = objectMapper.readTree(getChatMockResponse());
    ((ObjectNode) mockResponseNode.path("choices").get(0).path("message")).put("content", responseContent);
    return objectMapper.readValue(mockResponseNode.toString(), Chat.class);
  }

  @SneakyThrows
  public Transcription parameterizeTranscription(String output) {
    return objectMapper.readValue(String.format(getAudioMockResponse(), output), Transcription.class);
  }

  private String getAudioMockResponse() {
    return """
        {
          "text": "%s"
        }
        """;
  }

  @SneakyThrows
  private String getChatMockResponse() {
    return new String(Files.readAllBytes(Paths.get(MOCK_FILE_PATH)));
  }
}
