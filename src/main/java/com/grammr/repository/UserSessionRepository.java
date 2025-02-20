package com.grammr.repository;

import com.grammr.domain.entity.User;
import com.grammr.domain.entity.UserSession;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionRepository extends JpaRepository<UserSession, String> {

  Optional<UserSession> findBySessionToken(String sessionToken);

  void deleteAllByExpiresAtBefore(LocalDateTime now);

  void deleteByUser(User user);
}
