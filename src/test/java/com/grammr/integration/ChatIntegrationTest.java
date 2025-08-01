package com.grammr.integration;

import static com.grammr.config.web.AnonymousSessionFilter.ANON_COOKIE_NAME;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grammr.annotation.IntegrationTest;
import com.grammr.chat.controller.ChatController.AnalysisEnrichmentDto;
import com.grammr.chat.controller.dto.ChatInitializationDto;
import com.grammr.chat.controller.dto.ChatInitializedDto;
import com.grammr.chat.service.OpenAIChatService;
import com.grammr.chat.value.Message;
import com.grammr.domain.entity.Analysis;
import com.grammr.domain.entity.ChatMessage.Role;
import com.grammr.domain.entity.UserSpec;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.repository.AnalysisRepository;
import com.grammr.repository.ChatMessageRepository;
import com.grammr.repository.ChatRepository;
import jakarta.servlet.http.Cookie;
import java.util.List;
import lombok.SneakyThrows;
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
  @Autowired private AnalysisRepository analysisRepository;

  @BeforeEach
  void setup() {
    chatRepository.deleteAll();
    chatMessageRepository.deleteAll();
  }

  @Test
  @SneakyThrows
  void shouldCreateChat() {
    var chatInitializationDto = ChatInitializationDto.builder()
        .message("Hallo, wie geht es Ihnen?")
        .languageCode(LanguageCode.DE)
        .build();

    var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/chat")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(chatInitializationDto)))
        .andExpect(status().isOk()).andReturn();

    var chatInitializedDto = objectMapper.readValue(result.getResponse().getContentAsString(), ChatInitializedDto.class);
    var chatId = chatInitializedDto.chat().chatId();

    var chat = chatRepository.findByChatId(chatId).orElseThrow();
    assertThat(chat.getTotalTokens()).isGreaterThan(0);

    assertThat(chatMessageRepository.count()).isEqualTo(3);
  }

  @Test
  void shouldListUserChatsWhenAuthenticated() throws Exception {
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var auth = createUserAuthentication(user);
    String initialMessage = "Hallo, wie geht es Ihnen?";

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
  @SneakyThrows
  void shouldGetChatMessages() {
    String initialMessage = "Hallo, wie geht es Ihnen?";
    var chat = chatService.initializeChat(LanguageCode.DE, null, initialMessage);
    chatService.respond(null, chat.getChatId(), initialMessage);

    assertThat(chatMessageRepository.count()).isEqualTo(3);

    var response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/chat/{chatId}/messages", chat.getChatId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse();

    var messages = objectMapper.readValue(response.getContentAsString(), Message[].class);
    assertThat(messages).hasSize(3);
  }

  @Test
  @SneakyThrows
  void shouldTransferChatOwnership() {
    String initialMessage = "Hallo, wie geht es Ihnen?";
    var chatInitializationDto = ChatInitializationDto.builder()
        .message(initialMessage)
        .languageCode(LanguageCode.DE)
        .build();

    // Given a chat is initialized with no authentication
    var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/chat")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(chatInitializationDto)))
        .andExpect(status().isOk()).andReturn();

    // Retrieve the owner ID
    var chatInitializedDto = objectMapper.readValue(result.getResponse().getContentAsString(), ChatInitializedDto.class);
    var chatId = chatInitializedDto.chat().chatId();
    var chat = chatRepository.findByChatId(chatId).orElseThrow();

    assertThat(chat.getOwner()).isNotNull();
    assertThat(chat.getOwner().isAnonymous()).isTrue();
    var sessionId = chat.getOwner().getSessionId();

    // When the user creates an account
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var auth = createUserAuthentication(user);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/chat", chatId)
            .with(authentication(auth))
            .cookie(new Cookie(ANON_COOKIE_NAME, sessionId.toString()))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    assertThat(chatRepository.findByChatId(chatId).orElseThrow().getOwner().getId()).isEqualTo(user.getId());
  }

  @Test
  @SneakyThrows
  void shouldEnrichMessageWithAnalysisId() {
    String initialMessage = "Hallo, wie geht es Ihnen?";
    var chat = chatService.initializeChat(LanguageCode.DE, null, initialMessage);
    chatService.respond(null, chat.getChatId(), initialMessage);

    var messages = chatMessageRepository.findByChat(chat);
    var assistantMessage = messages.stream()
        .filter(message -> message.getRole().equals(Role.ASSISTANT))
        .findFirst()
        .orElseThrow();

    var analysis = analysisRepository.save(Analysis.from("Analysis text", LanguageCode.DE, List.of()));
    var enrichmentDto = new AnalysisEnrichmentDto(analysis.getAnalysisId());

    mockMvc.perform(MockMvcRequestBuilders.put("/api/v2/chat/{chatId}/messages/{messageId}", chat.getChatId(), assistantMessage.getMessageId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(enrichmentDto))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    assistantMessage = chatMessageRepository.findByMessageId(assistantMessage.getMessageId()).orElseThrow();
    assertThat(assistantMessage.getAnalysisId()).isNotNull();
  }
}
