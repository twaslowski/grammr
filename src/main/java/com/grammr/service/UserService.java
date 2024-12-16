package com.grammr.service;

import com.grammr.domain.entity.User;
import com.grammr.repository.UserRepository;
import com.grammr.telegram.exception.UserNotFoundException;
import com.grammr.telegram.service.UserInitializationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final UserInitializationService userInitializationService;

  public User createUserFromChatId(long chatId) {
    return userRepository.findByChatId(chatId).orElseGet(() ->
        userInitializationService.initializeUser(chatId));
  }

  public User findUserByChatId(long chatId) {
    return userRepository.findByChatId(chatId).orElseThrow(
        () -> new UserNotFoundException(chatId));
  }

  @Transactional
  public boolean toggleDebug(long chatId) {
    var user = findUserByChatId(chatId);
    user.toggleDebug();
    return user.debug();
  }
}
