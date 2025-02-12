package com.grammr.service;

import com.grammr.domain.entity.User;
import com.grammr.domain.exception.InvalidInputException;
import com.grammr.domain.exception.InvalidPasswordException;
import com.grammr.domain.exception.UserAlreadyExistsException;
import com.grammr.port.dto.UserRegistrationRequest;
import com.grammr.port.dto.UserRegistrationResponse;
import com.grammr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final SessionManager sessionManager;

  @Value("${spring.security.hashing.pepper}")
  private String pepper;

  @SneakyThrows
  public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
    if (!isValidEmail(request.email())) {
      throw new InvalidInputException("Invalid email format");
    }

    validatePassword(request.password());

    // Check if user already exists
    if (userRepository.findByEmail(request.email()).isPresent()) {
      throw new UserAlreadyExistsException(request.email());
    }

    String hashedPassword = passwordEncoder.encode(request.password() + pepper);

    var user = User.builder()
        .email(request.email())
        .password(hashedPassword)
        .build();
    userRepository.save(user);

    // Generate session token
    var session = sessionManager.createSession(user);

    return new UserRegistrationResponse(user.getId(), session.getSessionToken());
  }

  private void validatePassword(String password) {
    if (password.length() < 16) {
      throw new InvalidPasswordException("Password must be at least 16 characters long");
    }
  }

  private boolean isValidEmail(String email) {
    return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
  }

}
