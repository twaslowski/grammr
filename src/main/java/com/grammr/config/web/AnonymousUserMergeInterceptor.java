package com.grammr.config.web;

import com.grammr.chat.service.ChatPersistenceService;
import com.grammr.domain.entity.User;
import com.grammr.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnonymousUserMergeInterceptor implements HandlerInterceptor {

  /**
   * When a previously anonymous user registers with an external IDP,
   * their user data needs to be merged with their existing anonymous session.
   * This merge has to happen if a Session Token is present in the request,
   * but the user is authenticated and has an external IDP linked.
   */

  private final UserRepository userRepository;
  private final ChatPersistenceService chatPersistenceService;

  @Override
  public boolean preHandle(@NotNull HttpServletRequest request,
                           @NotNull HttpServletResponse response,
                           @NotNull Object handler) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null || !auth.isAuthenticated()) {
      return true;
    }

    try {
      // A merge should only occur if the user is authenticated
      if (auth.isAuthenticated()) {
        Optional<String> maybeAnonId = SessionCookieUtils.findSessionId(request);
        // If no ID present, they already have been merged
        if (maybeAnonId.isEmpty()) {
          return true;
        }

        // Retrieve the anonymous user, which has to be deleted
        String sessionId = maybeAnonId.get();
        Optional<User> maybeAnonUser = userRepository.findBySessionId(UUID.fromString(sessionId));
        if (maybeAnonUser.isEmpty()) {
          return true;
        }

        // Transfer chat ownership and delete the anonymous user
        User anonUser = maybeAnonUser.get();
        User authenticatedUser = (User) auth.getPrincipal();

        chatPersistenceService.transferChatOwnership(anonUser, authenticatedUser);

        anonUser.setDeleted(true);
        userRepository.save(anonUser);

        log.info("Merged anonymous user with ID {} into authenticated user with ID {}",
            anonUser.getId(), authenticatedUser.getId());
        SessionCookieUtils.clearSessionCookie(response);
      }
    } catch (Exception e) {
      // Generally, any errors here should not prevent the request from being processed
      log.error("Error during anonymous user merge", e);
      return true;
    }
    return true;
  }
}

