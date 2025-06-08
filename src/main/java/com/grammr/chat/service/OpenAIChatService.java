package com.grammr.chat.service;

import com.grammr.chat.value.Message;
import com.grammr.common.MessageUtil;
import com.grammr.domain.entity.Chat;
import com.grammr.domain.entity.ChatMessage;
import com.grammr.domain.entity.ChatMessage.Role;
import com.grammr.domain.entity.User;
import com.grammr.domain.enums.LanguageCode;
import com.openai.client.OpenAIClient;
import com.openai.core.http.StreamResponse;
import com.openai.models.ChatModel;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseInputItem;
import com.openai.models.responses.ResponseOutputText;
import com.openai.models.responses.ResponseStreamEvent;
import com.openai.models.responses.ResponseTextDeltaEvent;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAIChatService {

  private final OpenAIClient client;
  private final MessageUtil messageUtil;
  private final ChatPersistenceService chatPersistenceService;

  public Chat initializeChat(LanguageCode languageCode, @Nullable User user, String message) {
    var chat = chatPersistenceService.newChat(user, message);

    var systemPromptMessage = messageUtil.parameterizeMessage("openai.chat.prompt.system", languageCode);
    var systemPrompt = ChatMessage.from(systemPromptMessage, chat, Role.SYSTEM);

    chatPersistenceService.save(systemPrompt);
    return chat;
  }

  public Message respond(User user, UUID chatId, String message) {
    Chat chat = chatPersistenceService.retrieveChat(chatId, user);

    ChatMessage userMessage = ChatMessage.from(message, chat, Role.USER);
    List<ChatMessage> chatHistory = chatPersistenceService.getMessages(chat);

    List<ResponseInputItem> inputItems = buildInputItems(chatHistory, userMessage);

    ResponseCreateParams createParams = ResponseCreateParams.builder()
        .inputOfResponse(inputItems)
        .model(ChatModel.GPT_4O)
        .build();
    String responseText = createResponse(createParams);

    var response = ChatMessage.from(responseText, chat, Role.ASSISTANT);
    chatPersistenceService.save(List.of(userMessage, response));
    return Message.fromChatMessage(response);
  }

  private List<ResponseInputItem> buildInputItems(List<ChatMessage> chatHistory, ChatMessage userMessage) {
    return Stream.concat(chatHistory.stream(), Stream.of(userMessage))
        .map(Message::fromChatMessage)
        .map(Message::toEasyInputMessage)
        .map(ResponseInputItem::ofEasyInputMessage)
        .toList();
  }

  private String createResponse(ResponseCreateParams createParams) {
    return client.responses().create(createParams)
        .output().stream()
        .flatMap(item -> item.message().stream())
        .flatMap(message -> message.content().stream())
        .flatMap(content -> content.outputText().stream())
        .map(ResponseOutputText::text)
        .collect(Collectors.joining());
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
