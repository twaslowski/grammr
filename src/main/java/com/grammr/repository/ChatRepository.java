package com.grammr.repository;

import com.grammr.domain.entity.Chat;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID> {

}
