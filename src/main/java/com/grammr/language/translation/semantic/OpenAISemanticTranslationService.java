package com.grammr.language.translation.semantic;

import static java.lang.String.format;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.domain.value.language.SemanticTranslation;
import com.grammr.language.AbstractOpenAIService;
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
public class OpenAISemanticTranslationService extends AbstractOpenAIService implements SemanticTranslationService {

  public OpenAISemanticTranslationService(
      ObjectMapper objectMapper,
      SimpleOpenAI openAI,
      @Value("${openai.model.default-model}")
      String modelName
  ) {
    super(objectMapper, openAI, modelName);
  }

  public SemanticTranslation createSemanticTranslation(String phrase) {
    return openAIChatCompletion(generateUserMessage(List.of(phrase)), SemanticTranslation.class);
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
    return SystemMessage.of(format(
        "You translate sentences while retaining meaning as closely as possible. Return a JSON with the following structure: %s",
        objectMapper.writeValueAsString(new SemanticTranslation(
            "How are you doing",
            "Wie geht es dir?"
        ))));
  }

  public UserMessage generateUserMessage(List<String> params) {
    // todo: source (and perhaps target) language should be parameterizable
    return UserMessage.of(format("Translate the following phrase to English: %s", params.getFirst()));
  }
}
