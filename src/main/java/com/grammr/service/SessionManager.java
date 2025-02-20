package com.grammr.service;

import com.grammr.domain.entity.User;
import com.grammr.domain.entity.UserSession;
import com.grammr.repository.UserSessionRepository;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionManager {

  private final UserSessionRepository sessionRepository;
  private final SecureRandom secureRandom = new SecureRandom();

  public UserSession createSession(User user) {
    // Generate a cryptographically secure random token
    String sessionToken = generateSecureToken();

    // Create a new session record
    UserSession session = UserSession.builder()
        .user(user)
        .sessionToken(sessionToken)
        .createdAt(LocalDateTime.now())
        .lastAccessedAt(LocalDateTime.now())
        .expiresAt(LocalDateTime.now().plusDays(7))
        .build();

    // Save the session to the database
    return sessionRepository.save(session);
  }

  public Optional<User> validateSessionToken(String sessionToken) {
    return sessionRepository.findBySessionToken(sessionToken)
        .flatMap(this::validateSessionExpiry)
        .map(UserSession::getUser);
  }

  public Optional<UserSession> validateSessionExpiry(UserSession session) {
    if (LocalDateTime.now().isAfter(session.getExpiresAt())) {
      log.info("Expiring session {} for user {}", session.getId(), session.getUser().getId());
      sessionRepository.delete(session);
      return Optional.empty();
    }

    session.setLastAccessedAt(LocalDateTime.now());
    sessionRepository.save(session);
    log.debug("Updated session {} for user {}, new expiry: {}",
        session.getId(), session.getUser().getId(), session.getExpiresAt());
    return Optional.of(session);
  }

  private String generateSecureToken() {
    byte[] tokenBytes = new byte[32];
    secureRandom.nextBytes(tokenBytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
  }

  @Scheduled(fixedRate = 3600000)
  public void cleanupExpiredSessions() {
    log.info("Running job to clean up expired sessions");
    sessionRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());
  }

  public void deleteSession(User user) {
    sessionRepository.deleteByUser(user);
  }
}
