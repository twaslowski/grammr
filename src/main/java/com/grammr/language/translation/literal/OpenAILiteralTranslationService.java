package com.grammr.language.translation.literal;

import static java.lang.String.format;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.domain.value.ExampleValues;
import com.grammr.domain.value.language.LiteralTranslation;
import com.grammr.domain.value.language.TranslatedToken;
import com.grammr.language.AbstractOpenAIService;
import com.grammr.language.Tokenizer;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.ResponseFormat;
import io.github.sashirestela.openai.common.ResponseFormat.JsonSchema;
import io.github.sashirestela.openai.domain.chat.Chat;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OpenAILiteralTranslationService extends AbstractOpenAIService implements LiteralTranslationService {

  private final Tokenizer tokenizer;
  private final ObjectMapper objectMapper;
  private final SimpleOpenAI openAIClient;

  @Override
  public LiteralTranslation createLiteralTranslation(String phrase) {
    var tokens = tokenizer.tokenize(phrase);

    var translatedTokens = tokens.stream()
        .map(token -> generateChatRequest(phrase, token))
        .map(token -> executeChatRequest(token, TranslatedToken.class))
        .collect(Collectors.toSet());

    return new LiteralTranslation(phrase, translatedTokens);
  }

  @SneakyThrows
  protected <T> T executeChatRequest(ChatRequest request, Class<T> responseType) {
    var futureChat = openAIClient.chatCompletions().create(request);
    Chat response = futureChat.join();
    return objectMapper.readValue(response.firstContent(), responseType);
  }

  private ChatRequest generateChatRequest(String phrase, String token) {
    return ChatRequest.builder()
        .model("gpt-4o-mini")
        .message(getSystemMessage())
        .message(generateUserMessage(phrase, token))
        .responseFormat(getResponseFormat())
        .build();
  }

  private UserMessage generateUserMessage(String phrase, String token) {
    return UserMessage.of(format(
        "Translate the following word in the context of the following sentence: %s, %s",
        phrase, token)
    );
  }

  @Override
  protected UserMessage generateUserMessage(String param) {
    return null;
  }

  @Override
  @SneakyThrows
  protected SystemMessage getSystemMessage() {
    return SystemMessage.of(
        "Translate words into English within the context of a sentence. "
            + "Provide your responses in JSON format with the following structure:"
            + objectMapper.writeValueAsString(ExampleValues.translatedToken()));
  }

  @Override
  protected ResponseFormat getResponseFormat() {
    return ResponseFormat.jsonSchema(JsonSchema.builder()
        .name("translatedToken")
        .schemaClass(TranslatedToken.class)
        .build());
  }
}
