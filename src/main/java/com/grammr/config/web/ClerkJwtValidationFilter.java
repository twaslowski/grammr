package com.grammr.config.web;

import com.clerk.backend_api.helpers.jwks.AuthenticateRequest;
import com.clerk.backend_api.helpers.jwks.AuthenticateRequestOptions;
import com.clerk.backend_api.helpers.jwks.RequestState;
import com.grammr.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

  @Value("${clerk.secret-key}")
  private String secretKey;

  private final UserService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    RequestState requestState = AuthenticateRequest.authenticateRequest(
        extractHeaders(request), AuthenticateRequestOptions.secretKey(secretKey)
            .authorizedParty("https://localhost.com")
            .build()
    );
    if (!requestState.isSignedIn()) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    } else {
      requestState.claims().ifPresent(claims -> {
        var user = userService.getOrCreate(claims.getSubject());
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);
      });
      filterChain.doFilter(request, response);
    }
  }

  private Map<String, List<String>> extractHeaders(HttpServletRequest request) {
    return Collections.list(request.getHeaderNames())
        .stream()
        .collect(Collectors.toMap(
            Function.identity(),
            h -> Collections.list(request.getHeaders(h))
        ));
  }
}
