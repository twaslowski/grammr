package com.grammr.language.service.v2.translation.phrase;

import static com.grammr.language.service.v2.translation.phrase.Prompts.SYSTEM_PROMPT;
import static com.grammr.language.service.v2.translation.phrase.Prompts.USER_PROMPT;

import com.grammr.chat.value.Message;
import com.grammr.common.OpenAIResponsesService;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.language.v2.Translation;
import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenAIPhraseTranslationService extends OpenAIResponsesService implements PhraseTranslationService {

  private final OpenAIClient openAIClient;

  @Override
  public Translation translate(String phrase, LanguageCode sourceLanguage, LanguageCode targetLanguage) {
    String userPrompt = String.format(
        USER_PROMPT,
        targetLanguage,
        sanitizePhrase(phrase)
    );

    var systemPrompt = Message.systemPrompt(SYSTEM_PROMPT);
    var userMessage = Message.userMessage(userPrompt);
    var inputItems = buildInputItems(List.of(systemPrompt, userMessage));

    ResponseCreateParams createParams = ResponseCreateParams.builder()
        .inputOfResponse(inputItems)
        .model(ChatModel.GPT_4O)
        .build();

    Response openAIResponse = openAIClient.responses().create(createParams);
    String responseText = extractOutputText(openAIResponse);

    return new Translation(phrase, responseText, sourceLanguage, targetLanguage, null);
  }
}
