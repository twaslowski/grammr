package com.grammr.chat.service;

import static com.grammr.chat.service.Prompts.getSystemPrompt;

import com.grammr.chat.value.Message;
import com.grammr.common.OpenAIResponsesService;
import com.grammr.domain.entity.Chat;
import com.grammr.domain.entity.ChatMessage;
import com.grammr.domain.entity.ChatMessage.Role;
import com.grammr.domain.entity.User;
import com.grammr.domain.enums.LanguageCode;
import com.openai.client.OpenAIClient;
import com.openai.core.http.StreamResponse;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseInputItem;
import com.openai.models.responses.ResponseStreamEvent;
import com.openai.models.responses.ResponseTextDeltaEvent;
import com.openai.models.responses.ResponseUsage;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAIChatService extends OpenAIResponsesService {

  private final OpenAIClient client;
  private final ChatPersistenceService chatPersistenceService;

  public Chat initializeChat(LanguageCode languageCode, @Nullable User user, String message) {
    var chat = chatPersistenceService.newChat(user, message);

    var systemPromptMessage = getSystemPrompt(languageCode);
    var systemPrompt = ChatMessage.from(systemPromptMessage, chat, Role.SYSTEM, 0L);

    chatPersistenceService.save(systemPrompt);
    return chat;
  }

  public Message respond(User user, UUID chatId, String message) {
    Chat chat = chatPersistenceService.retrieveChat(chatId, user);

    ChatMessage userMessage = ChatMessage.from(message, chat, Role.USER, 0L);
    List<ChatMessage> chatHistory = chatPersistenceService.getMessages(chat);

    List<ResponseInputItem> inputItems = buildInputItems(chatHistory, userMessage);

    ResponseCreateParams createParams = ResponseCreateParams.builder()
        .inputOfResponse(inputItems)
        .model(ChatModel.GPT_4O)
        .build();
    Response openAIResponse = client.responses().create(createParams);

    String responseText = extractOutputText(openAIResponse);
    Long inputTokens = extractInputTokens(openAIResponse);
    Long outputTokens = extractOutputTokens(openAIResponse);

    userMessage.setTokenUsage(inputTokens);

    var response = ChatMessage.from(responseText, chat, Role.ASSISTANT, outputTokens);
    chat.incrementTokens(inputTokens + outputTokens);

    chatPersistenceService.save(chat, List.of(userMessage, response));
    return Message.fromChatMessage(response);
  }

  private Long extractInputTokens(Response response) {
    return response.usage().stream()
        .map(ResponseUsage::inputTokens)
        .reduce(0L, Long::sum);
  }

  private Long extractOutputTokens(Response response) {
    return response.usage().stream()
        .map(ResponseUsage::outputTokens)
        .reduce(0L, Long::sum);
  }

  @SneakyThrows
  public void streamChatResponse(List<Message> messages, Consumer<String> onChunk) {
    var inputItems = messages.stream()
        .map(Message::toEasyInputMessage)
        .map(ResponseInputItem::ofEasyInputMessage)
        .toList();

    ResponseCreateParams createParams = ResponseCreateParams.builder()
        .inputOfResponse(inputItems)
        .model(ChatModel.GPT_4O)
        .build();

    try (StreamResponse<ResponseStreamEvent> streamResponse =
        client.responses().createStreaming(createParams)) {

      streamResponse.stream()
          .flatMap(event -> event.outputTextDelta().stream())
          .map(ResponseTextDeltaEvent::delta)
          .forEach(onChunk);
    }
  }
}
