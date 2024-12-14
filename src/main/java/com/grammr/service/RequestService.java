package com.grammr.service;

import com.grammr.domain.entity.User;
import com.grammr.domain.exception.RequestNotFoundException;
import com.grammr.repository.RequestRepository;
import com.grammr.repository.UserRepository;
import com.grammr.telegram.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestService {

  private final RequestRepository requestRepository;
  private final UserRepository userRepository;

  public long retrieveRequestChatId(String requestId) {
    return requestRepository.findByRequestId(requestId)
        .orElseThrow(() -> new RequestNotFoundException(requestId))
        .getChatId();
  }

  public User retrieveUser(String requestId) {
    var chatId = retrieveRequestChatId(requestId);
    return userRepository.findByChatId(chatId).orElseThrow(() -> new UserNotFoundException(chatId));
  }
}
