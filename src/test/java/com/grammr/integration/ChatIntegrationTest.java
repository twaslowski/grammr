package com.grammr.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.grammr.annotation.IntegrationTest;
import com.grammr.chat.controller.v2.dto.ChatInitializationDto;
import com.grammr.chat.service.OpenAIChatService;
import com.grammr.chat.value.Message;
import com.grammr.domain.entity.ChatMessage.Role;
import com.grammr.domain.entity.UserSpec;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.repository.ChatMessageRepository;
import com.grammr.repository.ChatRepository;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@IntegrationTest
public class ChatIntegrationTest extends IntegrationTestBase {

  private static final String MOCK_FILE_PATH = "src/test/resources/templates/responses.json";

  @Autowired
  private ChatRepository chatRepository;

  @Autowired
  private ChatMessageRepository chatMessageRepository;

  @Autowired
  private OpenAIChatService chatService;

  @BeforeAll
  void setupWireMock() {
    wireMockServer = new WireMockServer(options().port(8089));
    wireMockServer.start();

    WireMock.configureFor("localhost", 8089);

    wireMockServer.stubFor(post(urlEqualTo("/v1/responses"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(getResponsesMock())));
  }

  @BeforeEach
  void setup() {
    chatRepository.deleteAll();
    chatMessageRepository.deleteAll();
  }

  @Test
  @SneakyThrows
  void shouldCreateChat() {
    var initialMessage = Message.builder()
        .role(Role.USER)
        .content("Hallo, wie geht es Ihnen?")
        .build();
    var chatInitializationDto = ChatInitializationDto.builder()
        .message(initialMessage)
        .languageCode(LanguageCode.DE)
        .build();

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/chat")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(chatInitializationDto)))
        .andExpect(status().isOk());

    assertThat(chatRepository.count()).isEqualTo(1);
    assertThat(chatMessageRepository.count()).isEqualTo(3);
  }

  @Test
  void shouldListUserChatsWhenAuthenticated() throws Exception {
    var user = userRepository.save(UserSpec.valid().build());
    var auth = createUserAuthentication(user);

    var initialMessage = Message.builder()
        .role(Role.USER)
        .content("Hallo, wie geht es Ihnen?")
        .build();
    var chatInitializationDto = ChatInitializationDto.builder()
        .message(initialMessage)
        .languageCode(LanguageCode.DE)
        .build();

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/chat")
            .contentType(MediaType.APPLICATION_JSON)
            .with(authentication(auth))
            .content(objectMapper.writeValueAsString(chatInitializationDto)))
        .andExpect(status().isOk());

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/chat")
            .with(authentication(auth))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].chatId").isNotEmpty());
  }

  @Test
  void shouldReturnUnauthorized() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/chat")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  void shouldGetChatMessages() {
    var chat = chatService.initializeChat(LanguageCode.DE, null);
    chatService.getResponse(chat.getChatId(),
        Message.builder()
            .role(Role.USER)
            .content("Hallo, wie geht es Ihnen?")
            .build());

    assertThat(chatMessageRepository.count()).isEqualTo(3);

    var response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/chat/{chatId}/messages", chat.getChatId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse();

    var messages = objectMapper.readValue(response.getContentAsString(), Message[].class);
    assertThat(messages).hasSize(3);
  }

  @SneakyThrows
  private String getResponsesMock() {
    return new String(Files.readAllBytes(Paths.get(MOCK_FILE_PATH)));
  }
}
