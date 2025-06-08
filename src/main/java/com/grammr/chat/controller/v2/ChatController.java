package com.grammr.chat.controller.v2;

import com.grammr.chat.controller.v2.dto.ChatDto;
import com.grammr.chat.controller.v2.dto.ChatInitializationDto;
import com.grammr.chat.service.ChatPersistenceService;
import com.grammr.chat.service.OpenAIChatService;
import com.grammr.chat.value.Message;
import com.grammr.domain.entity.User;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
  private final ChatPersistenceService chatPersistenceService;

  @PostMapping
  public ResponseEntity<Message> initializeChat(@AuthenticationPrincipal User user,
                                                @Valid @RequestBody ChatInitializationDto chatInitializationDto) {
    var chat = chatService.initializeChat(chatInitializationDto.languageCode(), user);
    var response = chatService.getResponse(chat.getChatId(), chatInitializationDto.message());

    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<ChatDto>> getUserChats(@AuthenticationPrincipal User user) {
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    var chats = chatPersistenceService.getUserChatIds(user).stream()
        .map(ChatDto::from)
        .toList();
    return ResponseEntity.ok(chats);
  }

  @GetMapping("/{chatId}/messages")
  public ResponseEntity<List<Message>> getChatMessages(@AuthenticationPrincipal User user, @Valid @PathVariable UUID chatId) {
    var messages = chatPersistenceService.getMessages(chatId, user);
    return ResponseEntity.ok(messages);
  }
}
