package com.grammr.service.language.recognition;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.common.MessageUtil;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.language.LanguageRecognition;
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
public class OpenAILanguageRecognitionService extends AbstractOpenAIService implements LanguageRecognitionService {

  public OpenAILanguageRecognitionService(
      ObjectMapper objectMapper,
      SimpleOpenAI openAI,
      MessageUtil messageUtil,
      @Value("${openai.completions.model}")
      String modelName
  ) {
    super(objectMapper, openAI, messageUtil, modelName);
  }

  public LanguageRecognition recognizeLanguage(String phrase) {
    return openAIChatCompletion(generateUserMessage(phrase), LanguageRecognition.class);
  }

  @Override
  protected ResponseFormat getResponseFormat() {
    return ResponseFormat.jsonSchema(JsonSchema.builder()
        .name("languageRecognition")
        .schemaClass(LanguageRecognition.class)
        .build());
  }

  @SneakyThrows
  @Override
  public SystemMessage getSystemMessage() {
    return SystemMessage.of(messageUtil.parameterizeMessage(
        "openai.language-recognition.prompt.system",
        objectMapper.writeValueAsString(new LanguageRecognition(LanguageCode.DE))));
  }

  public UserMessage generateUserMessage(String phrase) {
    return UserMessage.of(messageUtil.parameterizeMessage(
        "openai.language-recognition.prompt.user", phrase
    ));
  }
}
