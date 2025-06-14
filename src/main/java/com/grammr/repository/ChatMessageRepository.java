package com.grammr.repository;

import com.grammr.domain.entity.Chat;
import com.grammr.domain.entity.ChatMessage;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

  List<ChatMessage> findByChat(Chat chat);

  Optional<ChatMessage> findByMessageId(UUID messageId);
}
