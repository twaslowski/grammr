package com.grammr.chat.controller;

import com.grammr.chat.controller.dto.ChatDto;
import com.grammr.chat.controller.dto.ChatInitializationDto;
import com.grammr.chat.controller.dto.ChatInitializedDto;
import com.grammr.chat.service.ChatPersistenceService;
import com.grammr.chat.service.OpenAIChatService;
import com.grammr.chat.value.Message;
import com.grammr.domain.entity.Chat;
import com.grammr.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@Tag(name = "Chat", description = "v2 Chat operations")
@RestController("v2ChatController")
@RequestMapping("/api/v2/chat")
@RequiredArgsConstructor
public class ChatController {

  private final OpenAIChatService chatService;
  private final ChatPersistenceService chatPersistenceService;

  @Operation(summary = "Initialize a new chat session", description = "Initializes a chat for a user and returns the initialized chat and first response.")
  @PostMapping
  public ResponseEntity<ChatInitializedDto> initializeChat(
      @Parameter(description = "Authenticated user") @AuthenticationPrincipal User user,
      @Valid @RequestBody ChatInitializationDto chatInitializationDto) {
    var chat = chatService.initializeChat(chatInitializationDto.languageCode(), user, chatInitializationDto.message());
    var response = chatService.respond(user, chat.getChatId(), chatInitializationDto.message());

    return ResponseEntity.ok(new ChatInitializedDto(ChatDto.from(chat), response));
  }

  @Operation(summary = "Get all user chats", description = "Retrieves all chat sessions for the authenticated user.")
  @GetMapping
  public ResponseEntity<List<ChatDto>> getUserChats(
      @Parameter(description = "Authenticated user") @AuthenticationPrincipal User user) {
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    var chats = chatPersistenceService.getUserChatIds(user).stream()
        .map(ChatDto::from)
        .toList();
    return ResponseEntity.ok(chats);
  }

  @Operation(summary = "Get chat messages", description = "Retrieves all messages for a specific chat.")
  @GetMapping("/{chatId}/messages")
  public ResponseEntity<List<Message>> getChatMessages(@AuthenticationPrincipal User user, @PathVariable UUID chatId) {
    Chat chat = chatPersistenceService.retrieveChat(chatId, user);
    var messages = chatPersistenceService.getMessages(chat);
    var responseBody = messages.stream().map(Message::fromChatMessage).toList();
    return ResponseEntity.ok(responseBody);
  }

  @Operation(summary = "Add a message to chat", description = "Sends a new message in the chat and returns the response.")
  @PostMapping("/{chatId}/messages")
  public ResponseEntity<Message> addMessage(@AuthenticationPrincipal User user, @PathVariable UUID chatId, @RequestBody String message) {
    var response = chatService.respond(user, chatId, message);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{chatId}/messages/{messageId}")
  @Operation(summary = "Link analysis", description = """
      Links an analysis to a message in the chat.
      Although this endpoint modifies an object that might be owned by somebody, it is not authenticated
      so that it can be used anonymously. Both chatId and messageId are random UUIDs, which should preserve
      a degree of security.
      """)
  public ResponseEntity<Void> updateMessage(
      @PathVariable UUID chatId,
      @PathVariable UUID messageId, @RequestBody AnalysisEnrichmentDto dto) {
    chatPersistenceService.enrichMessageWithAnalysis(chatId, messageId, dto.analysisId);
    return ResponseEntity.ok().build();
  }

  public record AnalysisEnrichmentDto(
      UUID analysisId
  ) {

  }
}
