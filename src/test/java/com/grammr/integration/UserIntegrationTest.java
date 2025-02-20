package com.grammr.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grammr.annotation.IntegrationTest;
import com.grammr.port.dto.UserRegistrationRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@IntegrationTest
@AutoConfigureMockMvc
class UserIntegrationTest extends IntegrationTestBase {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @SneakyThrows
  void shouldCreateUserWithValidSessionToken() {
    var registrationRequest = new UserRegistrationRequest("user@username.com", "correct-battery-horse-staple");

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registrationRequest)))
        .andExpect(status().is(201))
        .andExpect(jsonPath("$.userId").isNumber())
        .andExpect(cookie().exists("session_token"));

    assertThat(userRepository.findByUsername("user@username.com")).isPresent();
    assertThat(userSessionRepository.findAll()).hasSize(1);
  }

  @Test
  @SneakyThrows
  void shouldReturnConflictWhenUserAlreadyExists() {
    var registrationRequest = new UserRegistrationRequest("test", "correct-battery-horse-staple");

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registrationRequest)))
        .andExpect(status().is(201))
        .andExpect(jsonPath("$.userId").isNumber())
        .andExpect(cookie().exists("session_token"));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registrationRequest)))
        .andExpect(status().is(409));
  }

  @Test
  @SneakyThrows
  void shouldReturnUnauthorizedIfUserDoesNotExistOrCredentialsMismatch() {
    var registrationRequest = new UserRegistrationRequest("test", "correct-battery-horse-staple");

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registrationRequest)))
        .andExpect(status().is(201))
        .andExpect(jsonPath("$.userId").isNumber())
        .andExpect(cookie().exists("session_token"));

    var badLoginRequest = new UserRegistrationRequest("test", "wrong-password");
    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(badLoginRequest)))
        .andExpect(status().is(401));

    var nonExistentUserRequest = new UserRegistrationRequest("non-existent-user", "password");
    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(nonExistentUserRequest)))
        .andExpect(status().is(401));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registrationRequest)))
        .andExpect(status().is(200));
  }
}
