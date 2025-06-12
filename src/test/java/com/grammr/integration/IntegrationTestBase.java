package com.grammr.integration;

import static com.grammr.integration.OpenAITestUtil.chatRequestContains;
import static com.grammr.integration.OpenAITestUtil.chatRequestEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.entity.User;
import com.grammr.domain.entity.UserSpec;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.language.SemanticTranslation;
import com.grammr.domain.value.language.TokenTranslation;
import com.grammr.language.service.v1.TokenService;
import com.grammr.language.service.v1.translation.semantic.OpenAISemanticTranslationService;
import com.grammr.repository.DeckRepository;
import com.grammr.repository.FlashcardRepository;
import com.grammr.repository.ParadigmRepository;
import com.grammr.repository.UserRepository;
import io.github.sashirestela.openai.OpenAI;
import io.github.sashirestela.openai.SimpleOpenAI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class IntegrationTestBase {

  @Autowired
  protected MockMvc mockMvc;

  @MockBean
  protected SimpleOpenAI openAIClient;

  @Autowired
  protected OpenAISemanticTranslationService semanticTranslationService;

  @Autowired
  protected TokenService tokenService;

  @Autowired
  protected OpenAITestUtil openAITestUtil;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected DeckRepository deckRepository;

  @Autowired
  protected FlashcardRepository flashcardRepository;

  @Autowired
  protected ParadigmRepository paradigmRepository;

  private final OpenAI.ChatCompletions chatCompletionsMock = mock(OpenAI.ChatCompletions.class);

  @BeforeEach
  public void setUp() {
    flashcardRepository.deleteAll();
    deckRepository.deleteAll();
    userRepository.deleteAll();
    paradigmRepository.deleteAll();
    reset(chatCompletionsMock);
  }

  protected Authentication createUserAuthentication() {
    var user = userRepository.save(UserSpec.valid().build());
    return new UsernamePasswordAuthenticationToken(user, null, List.of());
  }

  protected Authentication createUserAuthentication(User user) {
    return new UsernamePasswordAuthenticationToken(user, null, List.of());
  }

  @SneakyThrows
  protected void mockOpenAIResponseExactly(String exactPrompt, Object content) {
    var chat = openAITestUtil.parameterizeChatResponse(objectMapper.writeValueAsString(content));
    when(chatCompletionsMock.create(argThat(chatRequestEquals(exactPrompt))))
        .thenReturn(CompletableFuture.supplyAsync(() -> chat));
    when(openAIClient.chatCompletions()).thenReturn(chatCompletionsMock);
  }

  @SneakyThrows
  protected void mockOpenAIResponseFuzzy(String promptRegex, Object content) {
    var chat = openAITestUtil.parameterizeChatResponse(objectMapper.writeValueAsString(content));
    when(chatCompletionsMock.create(argThat(chatRequestContains(promptRegex))))
        .thenReturn(CompletableFuture.supplyAsync(() -> chat));
    when(openAIClient.chatCompletions()).thenReturn(chatCompletionsMock);
  }

  @SneakyThrows
  protected void mockSemanticTranslation(String source, String translation, LanguageCode to) {
    String prompt = semanticTranslationService.generateUserMessage(source, to).getContent().toString();
    mockOpenAIResponseExactly(prompt, SemanticTranslation.builder()
        .sourcePhrase(source)
        .translatedPhrase(translation)
        .build());
  }

  @SneakyThrows
  protected void mockTokenTranslation(String phrase, String word, TokenTranslation tokenTranslation) {
    mockOpenAIResponseFuzzy(
        "in the context of the following sentence",
        tokenTranslation);
  }
}
