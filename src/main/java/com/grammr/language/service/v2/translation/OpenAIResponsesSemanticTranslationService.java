package com.grammr.language.service.v2.translation;

import static com.grammr.language.service.v2.translation.Prompts.SYSTEM_PROMPT;
import static com.grammr.language.service.v2.translation.Prompts.USER_PROMPT;

import com.grammr.chat.value.Message;
import com.grammr.common.OpenAIResponsesService;
import com.grammr.domain.enums.LanguageCode;
import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenAIResponsesSemanticTranslationService extends OpenAIResponsesService implements PhraseTranslationService {

  private final OpenAIClient openAIClient;

  @Override
  public Translation translate(String phrase, LanguageCode targetLanguage, LanguageCode sourceLanguage) {
    String userPrompt = String.format(
        USER_PROMPT,
        sourceLanguage, targetLanguage,
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

  private String sanitizePhrase(String input) {
    return input.replaceAll("^\"|\"$", "")
        .replaceAll("\\p{Cntrl}&&[^\n\t]", "")
        .trim();
  }
}
