package com.grammr.config.web;

import static org.springframework.security.config.http.SessionCreationPolicy.NEVER;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
@EnableMethodSecurity
public class SpringSecurityConfiguration {

  @Value("${spring.cors.allowed-origin}")
  private String allowedOrigin;

  @Bean
  public SecurityFilterChain configureWebSecurity(HttpSecurity httpSecurity,
                                                  ClerkJwtValidationFilter clerkJwtValidationFilter,
                                                  AnonymousSessionFilter anonymousSessionFilter
  ) throws Exception {
    return httpSecurity
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(Customizer.withDefaults())
        .authorizeHttpRequests(this::configureRestAuthorizations)
        .addFilterBefore(clerkJwtValidationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(anonymousSessionFilter, ClerkJwtValidationFilter.class)
        .sessionManagement(session -> session.sessionCreationPolicy(NEVER))
        .build();
  }

  private void configureRestAuthorizations(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationRegistry) {
    authorizationRegistry
        .requestMatchers("/actuator/*").permitAll()
        .requestMatchers("/swagger-ui/**").permitAll()
        .requestMatchers("/v3/api-docs/**").permitAll()
        .requestMatchers("/api/v2/chat").permitAll()
        .requestMatchers("/api/v2/chat/**").hasAnyRole("USER", "ANONYMOUS")
        .requestMatchers("/api/v2/translations/**").permitAll()
        .requestMatchers("/api/v2/analysis").permitAll()
        .requestMatchers("/api/v2/analysis/*").permitAll()
        .requestMatchers("/api/v1/deck").hasRole("USER")
        .requestMatchers("/api/v1/deck/**").hasRole("USER")
        .requestMatchers("/api/v2/deck/**").hasRole("USER")
        .requestMatchers("/api/v1/flashcard").hasRole("USER")
        .requestMatchers("/api/v1/flashcard/**").hasRole("USER")
        .requestMatchers("/api/v1/**").permitAll()
        .anyRequest().authenticated();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of(allowedOrigin));
    config.setAllowedMethods(List.of("GET", "POST", "OPTIONS", "PUT", "DELETE"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", config);
    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
