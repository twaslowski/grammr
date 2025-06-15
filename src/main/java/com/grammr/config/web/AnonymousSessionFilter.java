package com.grammr.config.web;

import com.grammr.domain.entity.User;
import com.grammr.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnonymousSessionFilter extends OncePerRequestFilter {

  private static final String ANON_COOKIE_NAME = "anon_session_id";
  private static final Duration ANON_COOKIE_EXPIRY = Duration.ofDays(90);

  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(@NotNull HttpServletRequest request,
                                  @NotNull HttpServletResponse response,
                                  @NotNull FilterChain filterChain)
      throws ServletException, IOException {

    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

    if (currentAuth != null && currentAuth.isAuthenticated()) {
      filterChain.doFilter(request, response);
      return;
    }

    // Step 1: Try to find session ID from cookie
    String sessionId = findSessionIdFromCookie(request).orElse(null);

    User anonymousUser;
    if (sessionId != null) {
      anonymousUser = userRepository.findBySessionId(UUID.fromString(sessionId)).orElse(null);
    } else {
      sessionId = UUID.randomUUID().toString();
      anonymousUser = userRepository.save(User.anonymous(sessionId));
      addSessionCookie(response, sessionId);
    }

    if (anonymousUser == null) {
      anonymousUser = userRepository.save(User.anonymous(sessionId));
    }

    // Step 2: Set anonymous authentication
    Authentication auth = new UsernamePasswordAuthenticationToken(
        anonymousUser,
        null,
        List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))
    );
    SecurityContextHolder.getContext().setAuthentication(auth);

    filterChain.doFilter(request, response);
  }

  private Optional<String> findSessionIdFromCookie(HttpServletRequest request) {
    return Optional.ofNullable(request.getCookies())
        .flatMap(cookies -> Arrays.stream(cookies)
            .filter(c -> ANON_COOKIE_NAME.equals(c.getName()))
            .map(Cookie::getValue)
            .findFirst());
  }

  private void addSessionCookie(HttpServletResponse response, String sessionId) {
    Cookie cookie = new Cookie(ANON_COOKIE_NAME, sessionId);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge((int) ANON_COOKIE_EXPIRY.getSeconds());
    response.addCookie(cookie);
  }
}

