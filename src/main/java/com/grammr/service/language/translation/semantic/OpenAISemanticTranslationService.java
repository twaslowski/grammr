package com.grammr.service.language.translation.semantic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.common.MessageUtil;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.language.SemanticTranslation;
import com.grammr.service.language.AbstractOpenAIService;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.ResponseFormat;
import io.github.sashirestela.openai.common.ResponseFormat.JsonSchema;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OpenAISemanticTranslationService extends AbstractOpenAIService implements SemanticTranslationService {

  public OpenAISemanticTranslationService(
      ObjectMapper objectMapper,
      SimpleOpenAI openAI,
      MessageUtil messageUtil,
      @Value("${openai.completions.model}")
      String modelName
  ) {
    super(objectMapper, openAI, messageUtil, modelName);
  }

  public SemanticTranslation createSemanticTranslation(String phrase, LanguageCode to) {
    return openAIChatCompletion(generateUserMessage(phrase, to), SemanticTranslation.class).join();
  }

  @Override
  public ResponseFormat getResponseFormat() {
    return ResponseFormat.jsonSchema(JsonSchema.builder()
        .name("semanticTranslation")
        .schemaClass(SemanticTranslation.class)
        .build());
  }

  @SneakyThrows
  @Override
  public SystemMessage getSystemMessage() {
    return SystemMessage.of(messageUtil.parameterizeMessage(
        "openai.translation.semantic.prompt.system",
        objectMapper.writeValueAsString(new SemanticTranslation(
            "How are you doing",
            "Wie geht es dir?"
        ))));
  }

  public UserMessage generateUserMessage(String phrase, LanguageCode to) {
    // todo: source (and perhaps target) language should be parameterizable
    return UserMessage.of(messageUtil.parameterizeMessage("openai.translation.semantic.prompt.user",
        to.getLanguageName(), phrase));
  }


}
