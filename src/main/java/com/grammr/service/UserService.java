package com.grammr.service;

import com.grammr.domain.entity.User;
import com.grammr.domain.exception.InvalidInputException;
import com.grammr.domain.exception.InvalidPasswordException;
import com.grammr.domain.exception.UserAlreadyExistsException;
import com.grammr.domain.exception.UserNotFoundException;
import com.grammr.port.dto.UserRegistrationRequest;
import com.grammr.port.dto.UserRegistrationResponse;
import com.grammr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final SessionManager sessionManager;
  private final EncryptionService encryptionService;

  @Value("${spring.security.hashing.pepper}")
  private String pepper;

  @SneakyThrows
  public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
    validatePassword(request.password());

    var emailHash = encryptionService.computeEmailHash(request.email());

    if (userRepository.findByEmailHash(emailHash).isPresent()) {
      log.info("Rejecting user registration; email already exists");
      throw new UserAlreadyExistsException(request.email());
    }

    String hashedPassword = passwordEncoder.encode(request.password() + pepper);

    var user = User.builder()
        .email(encryptionService.encryptEmail(request.email()))
        .emailHash(emailHash)
        .username(request.username())
        .password(hashedPassword)
        .build();
    userRepository.save(user);

    // Generate session token
    var session = sessionManager.createSession(user);

    return new UserRegistrationResponse(user.getId(), session.getSessionToken());
  }

  public String login(UserRegistrationRequest request) throws InvalidInputException {
    var user = userRepository.findByEmailHash(encryptionService.computeEmailHash(request.email()))
        .orElseThrow(() -> new InvalidInputException("User not found"));

    if (!passwordEncoder.matches(request.password() + pepper, user.getPassword())) {
      throw new InvalidInputException("Invalid password");
    }

    var session = sessionManager.createSession(user);
    return session.getSessionToken();
  }

  @Transactional
  public void logout(User user) {
    sessionManager.deleteSession(user);
  }

  private void validatePassword(String password) {
    if (password.length() < 8) {
      log.info("Rejecting user registration due to insecure password");
      throw new InvalidPasswordException("Password must be at least 8 characters long");
    }
  }
}
