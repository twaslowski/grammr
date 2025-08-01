package com.grammr.language.service.v2.translation.word;

import static com.grammr.language.service.v2.translation.word.Prompts.NO_CONTEXT_SYSTEM_PROMPT;
import static com.grammr.language.service.v2.translation.word.Prompts.NO_CONTEXT_USER_PROMPT;
import static com.grammr.language.service.v2.translation.word.Prompts.SYSTEM_PROMPT;
import static com.grammr.language.service.v2.translation.word.Prompts.USER_PROMPT;

import com.grammr.chat.value.Message;
import com.grammr.common.OpenAIResponsesService;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.language.v2.WordTranslation;
import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAIWordTranslationService extends OpenAIResponsesService implements WordTranslationService {

  private final OpenAIClient openAIClient;

  @Override
  public WordTranslation translate(String word, String context, LanguageCode targetLanguage) {
    String userPrompt = String.format(
        USER_PROMPT,
        word,
        targetLanguage,
        sanitizePhrase(context)
    );

    var systemPrompt = Message.systemPrompt(SYSTEM_PROMPT);
    var userMessage = Message.userMessage(userPrompt);
    var inputItems = buildInputItems(List.of(systemPrompt, userMessage));

    ResponseCreateParams createParams = ResponseCreateParams.builder()
        .inputOfResponse(inputItems)
        .model(ChatModel.GPT_4O)
        .build();

    Response openAIResponse = openAIClient.responses().create(createParams);
    String translatedWord = extractOutputText(openAIResponse);
    log.info("OpenAI literal translation: '{}' -> '{}' (context: '{}')", word, translatedWord, context);
    return WordTranslation.builder()
        .source(word)
        .translation(translatedWord)
        .targetLanguage(targetLanguage)
        .context(context)
        .build();
  }

  @Override
  public WordTranslation translateWithoutContext(String word, LanguageCode targetLanguage) {
    String userPrompt = String.format(
        NO_CONTEXT_USER_PROMPT,
        word,
        targetLanguage
    );

    var systemPrompt = Message.systemPrompt(NO_CONTEXT_SYSTEM_PROMPT);
    var userMessage = Message.userMessage(userPrompt);
    var inputItems = buildInputItems(List.of(systemPrompt, userMessage));

    ResponseCreateParams createParams = ResponseCreateParams.builder()
        .inputOfResponse(inputItems)
        .model(ChatModel.GPT_4O)
        .build();

    Response openAIResponse = openAIClient.responses().create(createParams);
    String translatedWord = extractOutputText(openAIResponse);
    log.info("OpenAI literal translation: '{}' -> '{}' (no context)", word, translatedWord);
    return WordTranslation.builder()
        .source(word)
        .translation(translatedWord)
        .targetLanguage(targetLanguage)
        .context("")
        .build();
  }
}
