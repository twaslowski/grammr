package com.grammr.chat.controller.v2;

import com.grammr.chat.controller.v1.dto.ChatInitializationDto;
import com.grammr.chat.service.OpenAIChatService;
import com.grammr.chat.value.Message;
import com.grammr.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
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
  public ResponseEntity<Message> initializeChat(@AuthenticationPrincipal User user, ChatInitializationDto chatInitializationDto) {
    var response = chatService.initializeChat(chatInitializationDto.languageCode(),
        chatInitializationDto.message(),
        user
    );
    return ResponseEntity.ok(response);
  }
}
