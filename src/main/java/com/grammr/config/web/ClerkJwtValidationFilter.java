package com.grammr.config.web;

import com.clerk.backend_api.helpers.jwks.AuthenticateRequest;
import com.clerk.backend_api.helpers.jwks.AuthenticateRequestOptions;
import com.clerk.backend_api.helpers.jwks.RequestState;
import com.grammr.domain.entity.User;
import com.grammr.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClerkJwtValidationFilter extends OncePerRequestFilter {

  private static final String SESSION_COOKIE_NAME = "__session";

  @Value("${clerk.jwt-key}")
  private String jwtKey;

  @Value("${clerk.authorized-party}")
  private String authorizedParty;

  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(@NotNull HttpServletRequest request,
                                  @NotNull HttpServletResponse response,
                                  @NotNull FilterChain filterChain) throws ServletException, IOException {
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
      var user = getOrCreate(claims.getSubject());
      Authentication auth = new UsernamePasswordAuthenticationToken(
          user,
          null,
          List.of(new SimpleGrantedAuthority("ROLE_USER"))
      );
      SecurityContextHolder.getContext().setAuthentication(auth);
    });
    filterChain.doFilter(request, response);
  }

  // AuthenticateRequest.authenticateRequest() takes a Map that represents request headers.
  // For parsing Cookies, it uses HttpCookie.parse(String httpCookie), which expects a Set-Cookie
  // header, instead of a plain Cookie header. This function extracts the JWT correctly
  // and passes it. Refactor when behavior in the library is improved.
  private Map<String, List<String>> extractHeaders(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader("Authorization"))
        .map(this::fakeAuthenticationHeader)
        .or(() -> extractSessionCookie(request))
        .orElse(Collections.emptyMap());
  }

  private Optional<Map<String, List<String>>> extractSessionCookie(HttpServletRequest request) {
    return Optional.ofNullable(request.getCookies())
        .flatMap(cookies -> Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals(SESSION_COOKIE_NAME))
            .findFirst()
            .map(cookie -> fakeAuthenticationHeader(cookie.getValue())));
  }

  private Map<String, List<String>> fakeAuthenticationHeader(String token) {
    return Map.of("Authorization", Collections.singletonList("Bearer " + token));
  }

  public User getOrCreate(String id) {
    return userRepository.findByExternalId(id)
        .orElseGet(() -> userRepository.save(User.fromExternalId(id)));
  }
}
