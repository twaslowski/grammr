package com.grammr.service.language.translation.literal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.common.MessageUtil;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.language.ContextFreeWordTranslation;
import com.grammr.domain.value.language.WordTranslation;
import com.grammr.service.language.AbstractStructuredOpenAIService;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.ResponseFormat;
import io.github.sashirestela.openai.common.ResponseFormat.JsonSchema;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import java.util.List;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ContextFreeWordTranslationService extends AbstractStructuredOpenAIService {

  public ContextFreeWordTranslationService(
      ObjectMapper objectMapper,
      SimpleOpenAI openAI,
      MessageUtil messageUtil,
      @Value("${openai.completions.model}")
      String modelName
  ) {
    super(objectMapper, openAI, messageUtil, modelName);
  }

  public ContextFreeWordTranslation create(String word, LanguageCode to, LanguageCode from) {
    return openAIChatCompletion(
        generateUserMessage(word, to, from), ContextFreeWordTranslation.class
    ).join();
  }

  @Override
  @SneakyThrows
  protected SystemMessage getSystemMessage() {
    return SystemMessage.of(messageUtil.parameterizeMessage(
        "openai.translation.word.prompt.system",
        objectMapper.writeValueAsString(new ContextFreeWordTranslation(
            "Hund",
            List.of(
                new WordTranslation("dog", "general")
            )
        ))));
  }

  @Override
  protected ResponseFormat getResponseFormat() {
    return ResponseFormat.jsonSchema(JsonSchema.builder()
        .name("wordTranslation")
        .schemaClass(ContextFreeWordTranslation.class)
        .build());
  }

  public UserMessage generateUserMessage(String word, LanguageCode targetLanguage, LanguageCode sourceLanguage) {
    return UserMessage.of(messageUtil.parameterizeMessage("openai.translation.word.prompt.user",
        word, targetLanguage.getLanguageName(), sourceLanguage.getLanguageName()));
  }
}
