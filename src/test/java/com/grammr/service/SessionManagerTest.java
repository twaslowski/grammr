package com.grammr.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.grammr.domain.entity.UserSession;
import com.grammr.domain.entity.UserSpec;
import com.grammr.repository.UserSessionRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SessionManagerTest {

  @InjectMocks
  private SessionManager sessionManager;

  @Mock
  private UserSessionRepository sessionRepository;

  @Test
  void shouldCreateSession() {
    var user = UserSpec.valid().build();
    var argumentCaptor = ArgumentCaptor.forClass(UserSession.class);

    // when
    sessionManager.createSession(user);

    // then
    verify(sessionRepository).save(argumentCaptor.capture());
    var session = argumentCaptor.getValue();
    assertThat(session.getUser()).isEqualTo(user);
  }

  @Test
  void shouldReturnUserOnValidSession() {
    var user = UserSpec.valid().build();
    var session = UserSession.builder()
        .user(user)
        .sessionToken("token")
        .expiresAt(LocalDateTime.now().plusDays(7))
        .build();

    when(sessionRepository.findBySessionToken("token")).thenReturn(Optional.of(session));

    // when
    var result = sessionManager.validateSessionToken("token");

    // then
    assertThat(result).contains(user);
  }

  @Test
  void shouldReturnEmptyOptionalOnInvalidSessionToken() {
    when(sessionRepository.findBySessionToken("token")).thenReturn(Optional.empty());

    // when
    var result = sessionManager.validateSessionToken("token");

    // then
    assertThat(result).isEmpty();
  }
}