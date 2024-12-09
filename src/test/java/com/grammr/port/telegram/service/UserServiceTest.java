package com.grammr.port.telegram.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.grammr.domain.User;
import com.grammr.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserInitializationService userInitializationService;

  @InjectMocks
  private UserService userService;

  @Test
  void shouldReturnTrueOnUserCreation() {
    when(userRepository.findByTelegramId(1L)).thenReturn(Optional.empty());

    userService.createUserFromTelegramId(1L);

    verify(userInitializationService).initializeUser(1L);
  }

  @Test
  void shouldReturnFalseOnUserCreation() {
    when(userRepository.findByTelegramId(1L)).thenReturn(Optional.of(new User()));

    assertFalse(userService.createUserFromTelegramId(1L));
  }
}