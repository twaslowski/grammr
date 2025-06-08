package com.grammr.chat.service;

import com.grammr.chat.value.Message;
import com.grammr.common.MessageUtil;
import com.grammr.domain.entity.User;
import com.grammr.domain.enums.LanguageCode;
import com.openai.client.OpenAIClient;
import com.openai.core.http.StreamResponse;
import com.openai.models.ChatModel;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseInputItem;
import com.openai.models.responses.ResponseOutputMessage;
import com.openai.models.responses.ResponseOutputText;
import com.openai.models.responses.ResponseStreamEvent;
import com.openai.models.responses.ResponseTextDeltaEvent;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
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

  public Message initializeChat(LanguageCode languageCode, Message initialMessage,
                                @Nullable User user) {
    var systemPromptMessage = messageUtil.parameterizeMessage("openai.chat.prompt.system", languageCode);

    var chat = chatPersistenceService.newChat(user);

    var messages = List.of(
        Message.systemPrompt(systemPromptMessage),
        initialMessage
    );

    chatPersistenceService.save(chat, messages);


    var inputItems = messages.stream()
        .map(Message::toEasyInputMessage)
        .map(ResponseInputItem::ofEasyInputMessage)
        .toList();

    ResponseCreateParams createParams = ResponseCreateParams.builder()
        .inputOfResponse(inputItems)
        .model(ChatModel.GPT_4O)
        .build();

    List<ResponseOutputMessage> responseMessages = client.responses().create(createParams)
        .output().stream()
        .flatMap(item -> item.message().stream())
        .toList();

    String response = responseMessages.stream()
        .flatMap(message -> message.content().stream())
        .flatMap(content -> content.outputText().stream())
        .map(ResponseOutputText::text)
        .collect(Collectors.joining());

    return Message.fromAssistant(response);
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

  @SneakyThrows
  private String safeSleep(String s) {
    Thread.sleep(100);
    return s + " ";
  }
}
