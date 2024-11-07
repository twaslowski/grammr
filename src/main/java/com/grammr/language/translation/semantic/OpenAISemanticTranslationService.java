package com.grammr.language.translation.semantic;

import static java.lang.String.format;

import com.grammr.domain.value.language.SemanticTranslation;
import com.grammr.language.AbstractOpenAIService;
import io.github.sashirestela.openai.common.ResponseFormat;
import io.github.sashirestela.openai.common.ResponseFormat.JsonSchema;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OpenAISemanticTranslationService extends AbstractOpenAIService implements SemanticTranslationService {

  public SemanticTranslation createSemanticTranslation(String phrase) {
    return openAIChatCompletion(phrase, SemanticTranslation.class);
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

  @Override
  public UserMessage generateUserMessage(String phrase) {
    // todo: source (and perhaps target) language should be parameterizable
    return UserMessage.of(format("Translate the following phrase to English: %s", phrase));
  }
}
