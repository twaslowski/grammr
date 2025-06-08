package com.grammr.chat.service;

import com.grammr.chat.value.Message;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatPersistenceService {

  private final ChatRepository chatRepository;
  private final ChatMessageRepository chatMessageRepository;

  public Chat newChat(User user) {
    var chat = Chat.builder()
        .owner(user)
        .chatId(UUID.randomUUID())
        .build();
    return chatRepository.save(chat);
  }

  public void save(Chat chat, List<Message> messages) {
    var chatMessages = messages.stream()
        .map(message -> ChatMessage.from(message, chat))
        .toList();
    chatMessageRepository.saveAll(chatMessages);
  }

  public void save(Chat chat, Message message) {
    var chatMessage = ChatMessage.from(message, chat);
    chatMessageRepository.save(chatMessage);
  }

  public Chat getChat(UUID chatId) {
    return chatRepository.findByChatId(chatId);
  }

  public List<ChatMessage> getChatMessages(Chat chatId) {
    return chatMessageRepository.findByChat(chatId);
  }

  @PostAuthorize("returnObject.owner == null or returnObject.owner.id == #user?.id")
  public Chat retrieveChat(UUID chatId, @Nullable User user) {
    return chatRepository.findByChatId(chatId);
  }

  public List<Message> getMessages(UUID chatId, @Nullable User user) {
    Chat chat = retrieveChat(chatId, user);
    return chatMessageRepository.findByChat(chat)
        .stream()
        .map(Message::fromChatMessage)
        .toList();
  }

  public List<Chat> getUserChatIds(User user) {
    return chatRepository.findChatsByOwner(user)
        .stream()
        .toList();
  }
}
