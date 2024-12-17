package com.grammr.service.language.translation.literal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.common.MessageUtil;
import com.grammr.domain.value.ExampleValues;
import com.grammr.domain.value.language.TokenTranslation;
import com.grammr.service.language.AbstractOpenAIService;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.ResponseFormat;
import io.github.sashirestela.openai.common.ResponseFormat.JsonSchema;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OpenAITokenTranslationService extends AbstractOpenAIService {

  public OpenAITokenTranslationService(
      ObjectMapper objectMapper,
      SimpleOpenAI openAI,
      MessageUtil messageUtil,
      @Value("${openai.completions.model}")
      String modelName
  ) {
    super(objectMapper, openAI, messageUtil, modelName);
  }

  public CompletableFuture<TokenTranslation> createTranslation(String phrase, String word) {
    return openAIChatCompletion(generateUserMessage(phrase, word), TokenTranslation.class);
  }

  protected UserMessage generateUserMessage(String phrase, String token) {
    return UserMessage.of(messageUtil.parameterizeMessage(
        "openai.translation.literal.prompt.user",
        token, phrase
    ));
  }

  @Override
  @SneakyThrows
  protected SystemMessage getSystemMessage() {
    return SystemMessage.of(
        messageUtil.parameterizeMessage(
            "openai.translation.literal.prompt.system",
            objectMapper.writeValueAsString(ExampleValues.tokenTranslation())
        ));
  }

  @Override
  protected ResponseFormat getResponseFormat() {
    return ResponseFormat.jsonSchema(JsonSchema.builder()
        .name("translatedToken")
        .schemaClass(TokenTranslation.class)
        .build());
  }
}
