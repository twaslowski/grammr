package com.grammr.integration;

import static com.grammr.integration.OpenAITestUtil.chatRequestMatcher;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.annotation.IntegrationTest;
import com.grammr.common.MessageUtil;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.language.LanguageRecognition;
import com.grammr.domain.value.language.SemanticTranslation;
import com.grammr.domain.value.language.TokenTranslation;
import com.grammr.repository.UserRepository;
import com.grammr.service.AnalysisRequestService;
import com.grammr.service.TokenService;
import com.grammr.service.language.recognition.OpenAILanguageRecognitionService;
import com.grammr.service.language.translation.literal.OpenAILiteralTranslationService;
import com.grammr.service.language.translation.semantic.OpenAISemanticTranslationService;
import com.grammr.telegram.dto.response.TelegramResponse;
import com.grammr.telegram.dto.update.TelegramUpdate;
import io.github.sashirestela.openai.OpenAI;
import io.github.sashirestela.openai.SimpleOpenAI;
import java.util.concurrent.BlockingQueue;
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
  protected OpenAILiteralTranslationService literalTranslationService;

  @Autowired
  protected OpenAILanguageRecognitionService languageRecognitionService;

  @Autowired
  protected AnalysisRequestService analysisRequestService;

  @Autowired
  protected TokenService tokenService;

  @Autowired
  protected OpenAITestUtil openAITestUtil;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected MessageUtil messageUtil;

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected BlockingQueue<TelegramUpdate> incomingMessageQueue;

  @Autowired
  protected BlockingQueue<TelegramResponse> outgoingMessageQueue;

  private final OpenAI.ChatCompletions chatCompletionsMock = mock(OpenAI.ChatCompletions.class);

  @BeforeEach
  public void setUp() {
    incomingMessageQueue.clear();
    outgoingMessageQueue.clear();
    userRepository.deleteAll();
  }

  @SneakyThrows
  protected void mockOpenAIResponse(String promptRegex, Object content) {
    var chat = openAITestUtil.parameterizeResponse(objectMapper.writeValueAsString(content));
    when(chatCompletionsMock.create(argThat(chatRequestMatcher(promptRegex))))
        .thenReturn(CompletableFuture.supplyAsync(() -> chat));
    when(openAIClient.chatCompletions()).thenReturn(chatCompletionsMock);
  }

  @SneakyThrows
  protected void mockSemanticTranslation(String source, String translation, LanguageCode to) {
    String prompt = semanticTranslationService.generateUserMessage(source, to).getContent().toString();
    mockOpenAIResponse(prompt, SemanticTranslation.builder()
        .sourcePhrase(source)
        .translatedPhrase(translation)
        .build());
  }

  @SneakyThrows
  protected void mockLanguageRecognition(String source, LanguageCode languageCode) {
    String prompt = languageRecognitionService.generateUserMessage(source).getContent().toString();
    mockOpenAIResponse(prompt, LanguageRecognition.of(languageCode));
  }

  @SneakyThrows
  protected void mockTokenTranslation(String phrase, String word, TokenTranslation tokenTranslation) {
    mockOpenAIResponse(
        messageUtil.parameterizeMessage("openai.translation.literal.prompt.user", phrase, word),
        tokenTranslation);
  }
}
