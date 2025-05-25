package com.grammr.controller;

import com.grammr.controller.dto.ChatInitRequest;
import com.grammr.domain.value.Message;
import com.grammr.service.OpenAIChatService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

  private final OpenAIChatService chatService;

  @PostMapping
  public ResponseEntity<StreamingResponseBody> streamChat(@RequestBody List<Message> messages, HttpServletResponse response) {
    StreamingResponseBody body = outputStream ->
        chatService.streamChatResponse(messages, writeToOutputStream(outputStream));

    return ResponseEntity.ok()
        .contentType(MediaType.TEXT_PLAIN)
        .body(body);
  }

  @PostMapping("/initialize")
  public ResponseEntity<StreamingResponseBody> initializeChat(@RequestBody ChatInitRequest request, Message initialMessage, HttpServletResponse response) {
    StreamingResponseBody body = outputStream ->
        chatService.initializeChat(request.languageCode(), initialMessage, writeToOutputStream(outputStream));

    return ResponseEntity.ok()
        .contentType(MediaType.TEXT_PLAIN)
        .body(body);
  }

  @NotNull
  private static Consumer<String> writeToOutputStream(OutputStream outputStream) {
    return chunk -> {
      try {
        log.info("Streaming chunk: {}", chunk);
        outputStream.write((chunk).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
      } catch (IOException e) {
        log.error("Error writing to output stream", e);
      }
    };
  }
}

