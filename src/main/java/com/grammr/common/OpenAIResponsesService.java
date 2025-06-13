package com.grammr.common;

import com.grammr.chat.value.Message;
import com.grammr.domain.entity.ChatMessage;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseInputItem;
import com.openai.models.responses.ResponseOutputText;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class OpenAIResponsesService {

  protected List<ResponseInputItem> buildInputItems(List<ChatMessage> chatHistory, ChatMessage userMessage) {
    return Stream.concat(chatHistory.stream(), Stream.of(userMessage))
        .map(Message::fromChatMessage)
        .map(Message::toEasyInputMessage)
        .map(ResponseInputItem::ofEasyInputMessage)
        .toList();
  }

  protected List<ResponseInputItem> buildInputItems(List<Message> chatHistory) {
    return chatHistory.stream()
        .map(Message::toEasyInputMessage)
        .map(ResponseInputItem::ofEasyInputMessage)
        .toList();
  }

  protected String extractOutputText(Response response) {
    return response.output().stream()
        .flatMap(item -> item.message().stream())
        .flatMap(message -> message.content().stream())
        .flatMap(content -> content.outputText().stream())
        .map(ResponseOutputText::text)
        .collect(Collectors.joining());
  }

  protected String sanitizePhrase(String input) {
    return input.replaceAll("^\"|\"$", "")
        .replaceAll("\\p{Cntrl}&&[^\n\t]", "")
        .trim();
  }
}
