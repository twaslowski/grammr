package com.grammr.telegram.service;

import com.grammr.domain.entity.User;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInitializationService {

  private final UserRepository userRepository;

  @Value("${grammr.default.user.language_spoken}")
  private LanguageCode defaultLanguageSpoken;

  @Value("${grammr.default.user.language_learned}")
  private LanguageCode defaultLanguageLearned;

  public User initializeUser(long chatId) {
    var user = userRepository.save(User.builder()
        .chatId(chatId)
        .languageSpoken(defaultLanguageSpoken)
        .languageLearned(defaultLanguageLearned)
        .build());
    log.info("Created user {}", chatId);
    return user;
  }
}
