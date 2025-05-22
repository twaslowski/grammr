package com.grammr.service;

import com.grammr.domain.value.Message;
import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseInputItem;
import com.openai.models.responses.ResponseOutputText;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAIChatService {

  private final OpenAIClient client;

  @SneakyThrows
  public Stream<String> streamChatResponse(List<Message> messages) {
    var inputItems = messages.stream()
        .map(Message::convert)
        .map(ResponseInputItem::ofEasyInputMessage)
        .toList();

    ResponseCreateParams createParams = ResponseCreateParams.builder()
        .inputOfResponse(inputItems)
        .model(ChatModel.GPT_4O)
        .build();

    return client.responses().create(createParams).output().stream()
        .flatMap(item -> item.message().stream())
        .flatMap(message -> message.content().stream())
        .flatMap(content -> content.outputText().stream())
        .map(ResponseOutputText::text);
  }
}
