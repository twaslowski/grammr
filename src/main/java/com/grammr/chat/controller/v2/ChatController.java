package com.grammr.chat.controller.v2;

import com.grammr.chat.controller.v2.dto.ChatInitializationDto;
import com.grammr.chat.service.OpenAIChatService;
import com.grammr.chat.value.Message;
import com.grammr.domain.entity.User;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController("v2ChatController")
@RequestMapping("/api/v2/chat")
@RequiredArgsConstructor
public class ChatController {

  private final OpenAIChatService chatService;

  @PostMapping
  public ResponseEntity<Message> initializeChat(@AuthenticationPrincipal User user,
                                                @Valid @RequestBody ChatInitializationDto chatInitializationDto) {
    var chat = chatService.initializeChat(chatInitializationDto.languageCode(), user);
    var response = chatService.getResponse(chat.getChatId(), chatInitializationDto.message());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{chatId}/messages")
  public ResponseEntity<List<Message>> getChatMessages(@AuthenticationPrincipal User user, @Valid @PathVariable UUID chatId) {
    var messages = chatService.getMessages(chatId, user);
    return ResponseEntity.ok(messages);
  }
}
