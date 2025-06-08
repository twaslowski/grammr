package com.grammr.config.web;

import static org.springframework.security.config.http.SessionCreationPolicy.NEVER;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
public class SpringSecurityConfiguration {

  @Value("${spring.cors.allowed-origin}")
  private String allowedOrigin;

  @Bean
  public SecurityFilterChain configureWebSecurity(HttpSecurity httpSecurity,
                                                  ClerkJwtValidationFilter clerkJwtValidationFilter
  ) throws Exception {
    return httpSecurity
        .cors(Customizer.withDefaults())
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
        .requestMatchers("/api/v2/chat").permitAll()
        .requestMatchers("/api/v1/deck").authenticated()
        .requestMatchers("/api/v1/deck/**").authenticated()
        .requestMatchers("/api/v1/flashcard").authenticated()
        .requestMatchers("/api/v1/flashcard/**").authenticated()
        .requestMatchers("/api/v1/**").permitAll()
        .anyRequest().authenticated();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of(allowedOrigin));
    config.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
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
