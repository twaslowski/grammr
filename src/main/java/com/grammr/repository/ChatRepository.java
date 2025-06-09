package com.grammr.repository;

import com.grammr.domain.entity.Chat;
import com.grammr.domain.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

  Optional<Chat> findByChatId(UUID chatId);

  List<Chat> findChatsByOwner(User owner);
}
