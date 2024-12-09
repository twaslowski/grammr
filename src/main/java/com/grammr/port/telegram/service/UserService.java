package com.grammr.port.telegram.service;

import com.grammr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final UserInitializationService userInitializationService;

  public boolean createUserFromTelegramId(long telegramId) {
    return userRepository.findByTelegramId(telegramId)
        .map(user -> false)
        .orElseGet(() -> userInitializationService.initializeUser(telegramId));
  }
}
