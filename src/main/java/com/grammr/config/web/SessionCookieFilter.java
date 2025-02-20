package com.grammr.config.web;

import com.grammr.service.SessionManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class SessionCookieFilter extends OncePerRequestFilter {

  private final SessionManager sessionManager;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain) throws IOException, ServletException {
    Cookie[] cookies = request.getCookies();
    String sessionId = extractSessionId(cookies);
    if (sessionId != null) {
      sessionManager.validateSessionToken(sessionId)
          .ifPresent(user -> {
            Authentication auth = new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
            SecurityContextHolder.getContext().setAuthentication(auth);
          });
    }
    chain.doFilter(request, response);
  }

  private String extractSessionId(Cookie[] cookies) {
    if (cookies == null) {
      return null;
    }
    for (Cookie cookie : cookies) {
      if ("session_token".equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }
}

