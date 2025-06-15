package com.grammr.config.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import com.grammr.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@ExtendWith(MockitoExtension.class)
class AnonymousSessionFilterTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private AnonymousSessionFilter anonymousSessionFilter;

  @Test
  @SneakyThrows
  void shouldNotApplyFilterIfAuthenticationIsPresent() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);

    UsernamePasswordAuthenticationToken auth =
        new UsernamePasswordAuthenticationToken("user", "password", List.of());
    getContext().setAuthentication(auth);

    anonymousSessionFilter.doFilterInternal(request, response, filterChain);

    verify(userRepository, never()).findBySessionId(any(UUID.class));
  }
}