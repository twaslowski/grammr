package com.grammr.service.language.recognition;

import static java.lang.String.format;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.language.LanguageRecognition;
import com.grammr.service.language.AbstractOpenAIService;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.ResponseFormat;
import io.github.sashirestela.openai.common.ResponseFormat.JsonSchema;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenAILanguageRecognitionService extends AbstractOpenAIService implements LanguageRecognitionService {

  public OpenAILanguageRecognitionService(
      ObjectMapper objectMapper,
      SimpleOpenAI openAI,
      @Value("${openai.model.default-model}")
      String modelName
  ) {
    super(objectMapper, openAI, modelName);
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
    return SystemMessage.of(format(
        "You identify languages of phrases. Return a JSON with the following structure: %s",
        objectMapper.writeValueAsString(new LanguageRecognition(LanguageCode.DE))));
  }

  public UserMessage generateUserMessage(String phrase) {
    return UserMessage.of(format("Identify the language of the following phrase: %s", phrase));
  }
}
