package com.grammr.integration;

import com.grammr.annotation.IntegrationTest;
import com.grammr.port.dto.UserRegistrationRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@AutoConfigureMockMvc
class UserIntegrationTest extends IntegrationTestBase {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @SneakyThrows
  void shouldCreateUserWithValidSessionToken() {
    var registrationRequest = new UserRegistrationRequest("user@email.com", "correct-battery-horse-staple");

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registrationRequest)))
        .andExpect(status().is(201))
        .andExpect(jsonPath("$.userId").isNumber())
        .andExpect(cookie().exists("session_token"));

    assertThat(userRepository.findByEmail("user@email.com")).isPresent();
    assertThat(userSessionRepository.findAll()).hasSize(1);
  }
}
