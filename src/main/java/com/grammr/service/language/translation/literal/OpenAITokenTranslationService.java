package com.grammr.service.language.translation.literal;

import static java.lang.String.format;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.domain.value.ExampleValues;
import com.grammr.domain.value.language.TokenTranslation;
import com.grammr.service.language.AbstractOpenAIService;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.ResponseFormat;
import io.github.sashirestela.openai.common.ResponseFormat.JsonSchema;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
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
      @Value("${openai.model.default-model}")
      String modelName
  ) {
    super(objectMapper, openAI, modelName);
  }

  public TokenTranslation createTranslation(String phrase, String word) {
    return openAIChatCompletion(generateUserMessage(phrase, word), TokenTranslation.class);
  }

  protected UserMessage generateUserMessage(String phrase, String token) {
    return UserMessage.of(format(
        "Translate the following word in the context of the following sentence: %s, %s",
        phrase, token)
    );
  }

  @Override
  @SneakyThrows
  protected SystemMessage getSystemMessage() {
    return SystemMessage.of(
        "Translate words into English within the context of a sentence. "
            + "Provide your responses in JSON format with the following structure:"
            + objectMapper.writeValueAsString(ExampleValues.tokenTranslation()));
  }

  @Override
  protected ResponseFormat getResponseFormat() {
    return ResponseFormat.jsonSchema(JsonSchema.builder()
        .name("translatedToken")
        .schemaClass(TokenTranslation.class)
        .build());
  }
}
