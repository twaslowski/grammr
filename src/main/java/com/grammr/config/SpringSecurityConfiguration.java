package com.grammr.config;

import static org.springframework.security.config.http.SessionCreationPolicy.NEVER;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class SpringSecurityConfiguration {

  private static final String OBSERVABILITY_ROLE = "OBSERVABILITY";

  @Value("${spring.cors.allowed-origin}")
  private String allowedOrigin;

  @Bean
  public SecurityFilterChain configureWebSecurity(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(Customizer.withDefaults())
        .authorizeHttpRequests(this::configureRestAuthorizations)
        .sessionManagement(session -> session.sessionCreationPolicy(NEVER))
        .build();
  }

  @Bean
  public UrlBasedCorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of(allowedOrigin));
    configuration.setAllowedMethods(List.of("GET", "POST"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

//   todo: this needs to go, but the above cors configuration is not working
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(@NotNull CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins(allowedOrigin)
            .allowedMethods("GET", "POST", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600); // 1 hour
      }
    };
  }

  private void configureRestAuthorizations(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationRegistry) {
    authorizationRegistry
        .requestMatchers("/actuator/health").permitAll()
        .requestMatchers("/actuator/metrics").hasRole(OBSERVABILITY_ROLE)
        .requestMatchers("/actuator/prometheus").hasRole(OBSERVABILITY_ROLE)
        .requestMatchers("/api/v1/**").permitAll()
        .anyRequest().authenticated();
  }

  @Bean
  public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder,
                                               @Value("${spring.security.observability.password}") String observabilityUserPassword,
                                               @Value("${spring.security.observability.user}") String observabilityUserName
  ) {
    UserDetails user = User.builder()
        .username(observabilityUserName)
        .password(passwordEncoder.encode(observabilityUserPassword))
        .roles(OBSERVABILITY_ROLE).build();

    return new InMemoryUserDetailsManager(user);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
