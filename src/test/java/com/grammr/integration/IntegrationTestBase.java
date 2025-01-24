package com.grammr.integration;

import static com.grammr.integration.OpenAITestUtil.chatRequestContains;
import static com.grammr.integration.OpenAITestUtil.chatRequestEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.annotation.IntegrationTest;
import com.grammr.common.MessageUtil;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.language.LanguageRecognition;
import com.grammr.domain.value.language.SemanticTranslation;
import com.grammr.domain.value.language.TokenTranslation;
import com.grammr.port.outbound.InflectionPort;
import com.grammr.repository.RequestRepository;
import com.grammr.repository.UserRepository;
import com.grammr.service.AnalysisService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@IntegrationTest
@SpringBootTest
public class IntegrationTestBase {

  @MockBean
  protected SimpleOpenAI openAIClient;

  @MockBean
  protected TelegramClient telegramClient;

  @Autowired
  protected OpenAISemanticTranslationService semanticTranslationService;

  @Autowired
  protected OpenAILiteralTranslationService literalTranslationService;

  @Autowired
  protected OpenAILanguageRecognitionService languageRecognitionService;

  @Autowired
  protected AnalysisService fullAnalysisService;

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
  protected RequestRepository requestRepository;

  @Autowired
  protected BlockingQueue<TelegramUpdate> incomingMessageQueue;

  @Autowired
  protected BlockingQueue<TelegramResponse> outgoingMessageQueue;

  @Autowired
  protected EventAccumulator eventAccumulator;

  @Autowired
  protected InflectionPort inflectionPort;

  private final OpenAI.ChatCompletions chatCompletionsMock = mock(OpenAI.ChatCompletions.class);
  private final OpenAI.Audios audioMock = mock(OpenAI.Audios.class);

  @BeforeEach
  public void setUp() {
    incomingMessageQueue.clear();
    outgoingMessageQueue.clear();
    userRepository.deleteAll();
    requestRepository.deleteAll();
    eventAccumulator.reset();
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
  protected void mockLanguageRecognition(String source, LanguageCode languageCode) {
    String prompt = languageRecognitionService.generateUserMessage(source).getContent().toString();
    mockOpenAIResponseExactly(prompt, LanguageRecognition.of(languageCode));
  }

  @SneakyThrows
  protected void mockTokenTranslation(String phrase, String word, TokenTranslation tokenTranslation) {
    mockOpenAIResponseFuzzy(
        "in the context of the following sentence",
        tokenTranslation);
  }

  @SneakyThrows
  protected void mockAudioTranscription(String output) {
    var transcription = openAITestUtil.parameterizeTranscription(output);
    when(audioMock.transcribe(any())).thenReturn(CompletableFuture.supplyAsync(() -> transcription));
    when(openAIClient.audios()).thenReturn(audioMock);
  }
}
