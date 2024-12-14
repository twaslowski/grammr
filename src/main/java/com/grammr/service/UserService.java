package com.grammr.service;

import com.grammr.domain.entity.User;
import com.grammr.repository.UserRepository;
import com.grammr.telegram.service.UserInitializationService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final UserInitializationService userInitializationService;

  public User createUserFromTelegramId(long telegramId) {
    return userRepository.findByTelegramId(telegramId).orElseGet(() ->
        userInitializationService.initializeUser(telegramId));
  }

  public Optional<User> findUserByChatId(long telegramId) {
    return userRepository.findByTelegramId(telegramId);
  }
}
