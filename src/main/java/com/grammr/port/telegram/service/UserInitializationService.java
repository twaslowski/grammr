package com.grammr.port.telegram.service;

import com.grammr.domain.User;
import com.grammr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInitializationService {

  private final UserRepository userRepository;

  @Transactional
  public boolean initializeUser(long telegramId) {
    userRepository.save(User.builder()
        .telegramId(telegramId)
        .build());
    log.info("Created user {}", telegramId);
    return true;
  }
}
