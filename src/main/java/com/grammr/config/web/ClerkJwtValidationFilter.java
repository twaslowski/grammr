package com.grammr.config.web;

import com.clerk.backend_api.helpers.jwks.AuthenticateRequest;
import com.clerk.backend_api.helpers.jwks.AuthenticateRequestOptions;
import com.clerk.backend_api.helpers.jwks.RequestState;
import com.grammr.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpCookie;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class ClerkJwtValidationFilter extends OncePerRequestFilter {

  private static final String SESSION_COOKIE_NAME = "__session";

  @Value("${clerk.jwt-key}")
  private String jwtKey;

  @Value("${clerk.authorized-party}")
  private String authorizedParty;

  private final UserService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()) {
      filterChain.doFilter(request, response);
      return;
    }

    RequestState requestState = AuthenticateRequest.authenticateRequest(
        extractHeaders(request), AuthenticateRequestOptions.jwtKey(jwtKey)
            .authorizedParty(authorizedParty)
            .build()
    );

    requestState.claims().ifPresent(claims -> {
      var user = userService.getOrCreate(claims.getSubject());
      Authentication auth = new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
      SecurityContextHolder.getContext().setAuthentication(auth);
    });
    filterChain.doFilter(request, response);
  }

  // AuthenticateRequest.authenticateRequest() takes a Map that represents request headers.
  // For parsing Cookies, it uses HttpCookie.parse(String httpCookie), which expects a Set-Cookie
  // header, instead of a plain Cookie header. This function extracts the JWT correctly
  // and passes it. Refactor when behavior in the library is improved.
  private Map<String, List<String>> extractHeaders(HttpServletRequest request) {
    Optional<String> authorizationHeader = Optional.ofNullable(request.getHeader("Authorization"));
    if (authorizationHeader.isPresent()) {
      return fakeAuthenticationHeader(authorizationHeader.get());
    }

    Optional<Cookie> sessionCookie = Arrays.stream(request.getCookies())
        .filter(cookie -> cookie.getName().equals(SESSION_COOKIE_NAME))
        .findFirst();

    return sessionCookie.map(cookie -> fakeAuthenticationHeader(cookie.getValue()))
        .orElse(Map.of());
  }

  private Map<String, List<String>> fakeAuthenticationHeader(String token) {
    return Map.of("Authorization", Collections.singletonList("Bearer " + token));
  }
}
