package com.grammr.config.web;

import static com.grammr.config.web.SessionCookieUtils.addSessionCookie;
import static com.grammr.config.web.SessionCookieUtils.findSessionId;

import com.grammr.domain.entity.User;
import com.grammr.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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

  public static final String ANON_COOKIE_NAME = "anon_session_id";

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

    // Only create anonymous users for chat API requests
    String requestPath = request.getRequestURI();
    if (!requestPath.startsWith("/api/v2/chat")) {
      filterChain.doFilter(request, response);
      return;
    }

    String sessionId = findSessionId(request).orElse(null);

    User anonymousUser;
    if (sessionId != null) {
      anonymousUser = userRepository.findBySessionId(UUID.fromString(sessionId)).orElse(null);
    } else {
      sessionId = UUID.randomUUID().toString();
      anonymousUser = userRepository.save(User.anonymous(sessionId));
      addSessionCookie(response, sessionId);
      log.info("Created new anonymous user with session ID: {}", sessionId);
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
}
