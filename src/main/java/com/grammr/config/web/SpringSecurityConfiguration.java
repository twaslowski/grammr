package com.grammr.config.web;

import static org.springframework.security.config.http.SessionCreationPolicy.NEVER;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
public class SpringSecurityConfiguration {

  @Bean
  public SecurityFilterChain configureWebSecurity(HttpSecurity httpSecurity,
                                                  ClerkJwtValidationFilter clerkJwtValidationFilter
  ) throws Exception {
    return httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(Customizer.withDefaults())
        .authorizeHttpRequests(this::configureRestAuthorizations)
        .addFilterBefore(clerkJwtValidationFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(session -> session.sessionCreationPolicy(NEVER))
        .build();
  }

  private void configureRestAuthorizations(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationRegistry) {
    authorizationRegistry
        .requestMatchers("/actuator/*").permitAll()
        .requestMatchers("/api/v1/chat").permitAll()
        .requestMatchers("/api/v1/deck").authenticated()
        .requestMatchers("/api/v1/deck/**").authenticated()
        .requestMatchers("/api/v1/flashcard").authenticated()
        .requestMatchers("/api/v1/flashcard/**").authenticated()
        .requestMatchers("/api/v1/**").permitAll()
        .anyRequest().authenticated();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
