package com.grammr.integration;

import static com.grammr.integration.OpenAITestUtil.chatRequestContains;
import static com.grammr.integration.OpenAITestUtil.chatRequestEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.language.service.TokenService;
import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.language.SemanticTranslation;
import com.grammr.domain.value.language.TokenTranslation;
import com.grammr.repository.DeckRepository;
import com.grammr.repository.FlashcardRepository;
import com.grammr.repository.ParadigmRepository;
import com.grammr.repository.UserRepository;
import com.grammr.language.service.translation.semantic.OpenAISemanticTranslationService;
import io.github.sashirestela.openai.OpenAI;
import io.github.sashirestela.openai.SimpleOpenAI;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@IntegrationTest
@SpringBootTest
public class IntegrationTestBase {

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
  private final OpenAI.Audios audioMock = mock(OpenAI.Audios.class);

  @BeforeEach
  public void setUp() {
    flashcardRepository.deleteAll();
    deckRepository.deleteAll();
    userRepository.deleteAll();
    paradigmRepository.deleteAll();
    reset(audioMock, chatCompletionsMock);
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
