package com.grammr.service;

import com.grammr.domain.entity.User;
import com.grammr.domain.entity.UserSession;
import com.grammr.domain.exception.InvalidSessionException;
import com.grammr.repository.UserSessionRepository;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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

  public User validateSession(String sessionToken) {
    // Find the session in the database
    UserSession session = sessionRepository.findBySessionToken(sessionToken)
        .orElseThrow(() -> new InvalidSessionException("Session not found"));

    // Check if the session has expired
    if (LocalDateTime.now().isAfter(session.getExpiresAt())) {
      sessionRepository.delete(session);
      throw new InvalidSessionException("Session has expired");
    }

    // Update last accessed time
    session.setLastAccessedAt(LocalDateTime.now());
    sessionRepository.save(session);

    return session.getUser();
  }

  private String generateSecureToken() {
    byte[] tokenBytes = new byte[32];
    secureRandom.nextBytes(tokenBytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
  }

  @Scheduled(fixedRate = 3600000)
  public void cleanupExpiredSessions() {
    sessionRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());
  }
}
