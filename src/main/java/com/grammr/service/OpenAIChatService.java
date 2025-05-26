package com.grammr.service;

import com.grammr.common.MessageUtil;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.Message;
import com.openai.client.OpenAIClient;
import com.openai.core.http.StreamResponse;
import com.openai.models.ChatModel;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseInputItem;
import com.openai.models.responses.ResponseStreamEvent;
import com.openai.models.responses.ResponseTextDeltaEvent;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAIChatService {

  private final OpenAIClient client;
  private final MessageUtil messageUtil;

  private static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";

  public void initializeChat(LanguageCode languageCode, Message initialMessage, Consumer<String> onChunk) {
    var systemPromptMessage = messageUtil.parameterizeMessage("openai.chat.prompt.system", languageCode);
    var messages = List.of(
        Message.systemPrompt(systemPromptMessage),
        initialMessage
    );
    var inputItems = messages.stream()
        .map(Message::toEasyInputMessage)
        .map(ResponseInputItem::ofEasyInputMessage)
        .toList();

    Arrays.stream(LOREM_IPSUM.split(" "))
        .map(this::safeSleep)
        .forEach(onChunk);

//    try (StreamResponse<ResponseStreamEvent> streamResponse =
//        client.responses().createStreaming(createParams)) {
//      streamResponse.stream()
//          .flatMap(event -> event.outputTextDelta().stream())
//          .map(ResponseTextDeltaEvent::delta)
//          .forEach(onChunk);
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
