package com.grammr.chat.service;

import com.grammr.domain.entity.Chat;
import com.grammr.domain.entity.ChatMessage;
import com.grammr.domain.entity.User;
import com.grammr.repository.ChatMessageRepository;
import com.grammr.repository.ChatRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatPersistenceService {

  private final ChatRepository chatRepository;
  private final ChatMessageRepository chatMessageRepository;

  public Chat newChat(User user, String message) {
    var chat = Chat.builder()
        .owner(user)
        .chatId(UUID.randomUUID())
        .totalTokens(0L)
        .summary(message.substring(0, Math.min(message.length(), 100)))
        .build();
    return chatRepository.save(chat);
  }

  public void save(List<ChatMessage> message) {
    chatMessageRepository.saveAll(message);
  }

  public void save(ChatMessage message) {
    chatMessageRepository.save(message);
  }

  public void save(Chat chat, List<ChatMessage> message) {
    chatRepository.save(chat);
    chatMessageRepository.saveAll(message);
  }

  @PostAuthorize("returnObject.owner == null or returnObject.owner.id == #user?.id")
  public Chat retrieveChat(UUID chatId, @Nullable User user) {
    return chatRepository.findByChatId(chatId).orElseThrow();
  }

  public List<ChatMessage> getMessages(Chat chat) {
    return chatMessageRepository.findByChat(chat);
  }

  public List<Chat> getUserChatIds(User user) {
    return chatRepository.findChatsByOwner(user)
        .stream()
        .toList();
  }
}
