package com.grammr.port.telegram.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.grammr.repository.UserRepository;
import com.grammr.service.UserService;
import com.grammr.telegram.service.UserInitializationService;
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
  void shouldCreateUserIfNotExists() {
    when(userRepository.findByChatId(1L)).thenReturn(Optional.empty());

    userService.createUserFromChatId(1L);

    verify(userInitializationService).initializeUser(1L);
  }
}