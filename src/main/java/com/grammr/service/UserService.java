package com.grammr.service;

import com.grammr.domain.entity.User;
import com.grammr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

  private final UserRepository userRepository;

  public User getOrCreate(String id) {
    return userRepository.findByExternalId(id)
        .orElseGet(() -> userRepository.save(User.fromExternalId(id)));
  }
}
