package com.grammr.controller;

import com.grammr.domain.value.Message;
import com.grammr.service.OpenAIChatService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  public ResponseEntity<StreamingResponseBody> streamChat(@RequestBody List<Message> messages) {
    StreamingResponseBody body = outputStream -> {
      chatService.streamChatResponse(messages).forEach(
          chunk -> {
            try {
              outputStream.write(chunk.getBytes());
              outputStream.flush();
            } catch (IOException e) {
              log.error("Error writing to output stream", e);
            }
          }
      );
    };

    return ResponseEntity.ok()
        .contentType(MediaType.TEXT_PLAIN)
        .body(body);
  }
}

