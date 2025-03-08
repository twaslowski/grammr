package com.grammr.service.language.translation.literal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.common.MessageUtil;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.ExampleValues;
import com.grammr.domain.value.language.Token;
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

  public TokenTranslation createTranslation(String phrase, Token token, LanguageCode targetLanguage) {
    var completionFuture = openAIChatCompletion(generateUserMessage(phrase, token.text(), targetLanguage), TokenTranslation.class);
    var tokenTranslation = completionFuture.join();
    tokenTranslation.setIndex(token.index());
    return tokenTranslation;
  }

  public TokenTranslation createTranslationForSingleWord(String phrase, String word, LanguageCode targetLanguage) {
    var completionFuture = openAIChatCompletion(generateUserMessage(phrase, word, targetLanguage), TokenTranslation.class);
    var tokenTranslation = completionFuture.join();
    tokenTranslation.setIndex(-1);
    return tokenTranslation;
  }


  protected UserMessage generateUserMessage(String phrase, String token, LanguageCode targetLanguage) {
    return UserMessage.of(messageUtil.parameterizeMessage(
        "openai.translation.literal.prompt.user",
        token, targetLanguage.getLanguageName(), phrase
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
