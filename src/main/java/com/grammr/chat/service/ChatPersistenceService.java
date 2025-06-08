package com.grammr.chat.service;

import com.grammr.chat.value.Message;
import com.grammr.domain.entity.Chat;
import com.grammr.domain.entity.ChatMessage;
import com.grammr.domain.entity.User;
import com.grammr.repository.ChatMessageRepository;
import com.grammr.repository.ChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        .build();
    return chatRepository.save(chat);
  }

  public void save(Chat chat, List<Message> messages) {
    var chatMessages = messages.stream()
        .map(message -> ChatMessage.from(message, chat))
        .toList();
    chatMessageRepository.saveAll(chatMessages);
  }
}
