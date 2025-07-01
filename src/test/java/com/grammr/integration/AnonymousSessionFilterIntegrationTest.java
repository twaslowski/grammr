package com.grammr.integration;

import static com.grammr.config.web.AnonymousSessionFilter.ANON_COOKIE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grammr.annotation.IntegrationTest;
import com.grammr.chat.service.ChatPersistenceService;
import com.grammr.config.web.AnonymousSessionFilter;
import com.grammr.domain.entity.User;
import jakarta.servlet.http.Cookie;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MvcResult;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@IntegrationTest
class AnonymousSessionFilterIntegrationTest extends IntegrationTestBase {

  @Autowired
  private ChatPersistenceService chatPersistenceService;

  @MockitoSpyBean
  private AnonymousSessionFilter anonymousSessionFilter;

  @Test
  @SneakyThrows
  void shouldCreateAnonymousUser() {
    MvcResult result = mockMvc.perform(get("/api/v2/chat"))
        .andExpect(status().isOk())
        .andReturn();

    // Check cookie in response
    Cookie setCookie = result.getResponse().getCookie(ANON_COOKIE_NAME);
    assertThat(setCookie).isNotNull();
    assertThat(ANON_COOKIE_NAME).isEqualTo(setCookie.getName());
    assertThat(setCookie.getValue()).isNotEmpty();

    // Confirm user was persisted
    assertThat(userRepository.findBySessionId(UUID.fromString(setCookie.getValue()))).isPresent();
  }

  @Test
  @SneakyThrows
  void shouldNotCreateAnonymousUserOnIrrelevantEndpoint() {
    MvcResult result = mockMvc.perform(get("/actuator/health"))
        .andExpect(status().isOk())
        .andReturn();

    // Check cookie in response
    Cookie setCookie = result.getResponse().getCookie(ANON_COOKIE_NAME);
    assertThat(setCookie).isNull();

    assertThat(userRepository.findAll()).isEmpty();
  }


  @Test
  void shouldLoadAnonymousUserWithSessionTokenPresent() throws Exception {
    String sessionId = UUID.randomUUID().toString();
    var user = userRepository.save(User.anonymous(sessionId));

    var chat = chatPersistenceService.newChat(user, "hello world");

    mockMvc.perform(get("/api/v2/chat")
            .cookie(new Cookie(ANON_COOKIE_NAME, sessionId)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].chatId").isNotEmpty())
        .andExpect(jsonPath("$[0].chatId").value(chat.getChatId().toString()));
  }
}