package com.grammr.config.web;

import com.grammr.repository.ChatRepository;
import com.grammr.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AnonymousUserMergeInterceptor implements HandlerInterceptor {

  private final UserRepository userRepository;
  private final ChatRepository chatRepository;

  @Override
  public boolean preHandle(@NotNull HttpServletRequest request,
                           @NotNull HttpServletResponse response,
                           @NotNull Object handler) throws Exception {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

//    if (auth == null || !(auth.getPrincipal() instanceof User authUser) || !authUser.isAuthenticatedUser()) {
//      return true; // Only proceed if authenticated
//    }
//
//    Optional<String> maybeAnonId = userRepository.
//    if (maybeAnonId.isEmpty()) {
//      return true;
//    }
//
//    String sessionId = maybeAnonId.get();
//
//    Optional<User> maybeAnonUser = userRepository.findBySessionId(sessionId);
//    if (maybeAnonUser.isEmpty() || maybeAnonUser.get().isDeleted()) {
//      return true;
//    }
//
//    User anonUser = maybeAnonUser.get();
//    if (anonUser.getId().equals(authUser.getId())) {
//      return true; // already merged
//    }
//
//    chatRepository.reassignChatsFromTo(anonUser.getId(), authUser.getId());

    // SessionCookieUtils.clearSessionCookie(response);

    return true;
  }
}

