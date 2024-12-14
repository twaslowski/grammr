package com.grammr.telegram.service;

import com.grammr.domain.entity.User;
import com.grammr.domain.enums.LanguageCode;
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
  public User initializeUser(long telegramId) {
    var user = userRepository.save(User.builder()
        .telegramId(telegramId)
        .languageSpoken(LanguageCode.DE)
        .languageLearned(LanguageCode.RU)
        .build());
    log.info("Created user {}", telegramId);
    return user;
  }
}
