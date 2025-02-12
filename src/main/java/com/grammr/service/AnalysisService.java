package com.grammr.service;

import com.grammr.domain.event.AnalysisRequest;
import com.grammr.domain.value.AnalysisComponentRequest;
import com.grammr.domain.value.FullAnalysis;
import com.grammr.domain.value.language.SemanticTranslation;
import com.grammr.service.language.morphology.MorphologyService;
import com.grammr.service.language.recognition.LanguageRecognitionService;
import com.grammr.service.language.translation.literal.LiteralTranslationService;
import com.grammr.service.language.translation.semantic.SemanticTranslationService;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Builder
@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisService {

  private static final int MAX_TOKENS = 15;

  private final ExecutorService executorService = Executors.newCachedThreadPool();
  private final SemanticTranslationService semanticTranslationService;
  private final LanguageRecognitionService languageRecognitionService;
  private final LiteralTranslationService literalTranslationService;
  private final TokenService tokenService;
  private final MorphologyService morphologyService;

  /**
   * User speaks Language A and learns Language B, and that the source phrase is in Language B.
   * Translates the sentence to Language A; also tokenizes the original sentence and enriches the tokens
   * with literal translations and morphological analysis.
   * @param analysisRequest AnalysisRequest object containing the source phrase and user languages.
   * @return FullAnalysis object containing translation and enriched tokens.
   */
  public FullAnalysis simpleAnalyze(AnalysisRequest analysisRequest) {
    var sourcePhrase = analysisRequest.phrase();
    var tokens = tokenService.tokenize(sourcePhrase);

    var analysisComponentRequest = AnalysisComponentRequest.builder()
        .phrase(sourcePhrase)
        .tokens(tokens)
        .sourceLanguage(analysisRequest.userLanguageLearned())
        .targetLanguage(analysisRequest.userLanguageSpoken())
        .build();

    var semanticTranslationFuture = supplyAsync(() -> semanticTranslationService.createAnalysisComponent(analysisComponentRequest));
    var literalTranslationFuture = supplyAsync(() -> literalTranslationService.createAnalysisComponent(analysisComponentRequest));
    var grammaticalAnalysisFuture = supplyAsync(() -> morphologyService.createAnalysisComponent(analysisComponentRequest));

    awaitAll(literalTranslationFuture, grammaticalAnalysisFuture);

    var literalTranslation = join(literalTranslationFuture).getTokenTranslations();
    var grammaticalAnalysis = join(grammaticalAnalysisFuture);
    var semanticAnalysis = join(semanticTranslationFuture);
    tokens = tokenService.enrichTokens(tokens, literalTranslation, grammaticalAnalysis);

    return FullAnalysis.builder()
        .semanticTranslation(semanticAnalysis)
        .sourcePhrase(sourcePhrase)
        .analyzedTokens(tokens)
        .build();
  }

  /**
   * User speaks Language A and learns Language B, and that the source phrase is in Language A.
   * A translation to Language B is created; afterward, the sentence is tokenized and enriched with
   * literal translations and morphological analysis.
   * @param analysisRequest AnalysisRequest object containing the source phrase and user languages.
   * @return FullAnalysis object containing translation and enriched tokens.
   */

  public FullAnalysis translateAndAnalyze(AnalysisRequest analysisRequest) {
    var sourcePhrase = analysisRequest.phrase();

    var translationRequest = AnalysisComponentRequest.builder()
        .sourceLanguage(analysisRequest.userLanguageSpoken())
        .targetLanguage(analysisRequest.userLanguageLearned())
        .phrase(sourcePhrase)
        .build();

    var semanticTranslation = semanticTranslationService.createAnalysisComponent(translationRequest);
    log.info("Translated phrase: '{}'; using it as base for further analysis", semanticTranslation.getTranslatedPhrase());
    var translation = semanticTranslation.getTranslatedPhrase();
    var tokens = tokenService.tokenize(translation);

    var analysisComponentRequest = AnalysisComponentRequest.builder()
        .phrase(semanticTranslation.getTranslatedPhrase())
        .sourceLanguage(analysisRequest.userLanguageLearned())
        .targetLanguage(analysisRequest.userLanguageSpoken())
        .tokens(tokens)
        .build();

    var literalTranslationFuture = supplyAsync(() -> literalTranslationService.createAnalysisComponent(analysisComponentRequest));
    var grammaticalAnalysisFuture = supplyAsync(() -> morphologyService.createAnalysisComponent(analysisComponentRequest));
    awaitAll(literalTranslationFuture, grammaticalAnalysisFuture);
    var literalTranslation = join(literalTranslationFuture).getTokenTranslations();
    var grammaticalAnalysis = join(grammaticalAnalysisFuture);

    tokens = tokenService.enrichTokens(tokens, literalTranslation, grammaticalAnalysis);
    return FullAnalysis.builder()
        .sourcePhrase(sourcePhrase)
        .semanticTranslation(semanticTranslation)
        .analyzedTokens(tokens)
        .build();
  }

  /**
   * Deprecated. Achieves language agnosticism by detecting the language of the source phrase,
   * at the cost of speed at clarity as to what the application actually does.
   * @param analysisRequest AnalysisRequest object containing the source phrase and user languages.
   * @return FullAnalysis object containing translation and enriched tokens.
   */
  @Deprecated
  public FullAnalysis processFullAnalysisRequest(AnalysisRequest analysisRequest) {
    var sourcePhrase = analysisRequest.phrase();
    log.info("Processing analysis request for source phrase: '{}'", sourcePhrase);

    var analyisComponentRequest = AnalysisComponentRequest.from(analysisRequest);
    var sourceLanguage = languageRecognitionService.createAnalysisComponent(analyisComponentRequest);

    // If detected language is learned language
    if (sourceLanguage.getLanguageCode().equals(analyisComponentRequest.getSourceLanguage())) {
      return createAnalysis(analysisRequest, analyisComponentRequest);
      // If detected language is spoken language
    } else if (sourceLanguage.getLanguageCode().equals(analysisRequest.userLanguageSpoken())) {
      return createReverseAnalysis(analysisRequest, analyisComponentRequest.reverseLanguages());
    } else {
      return FullAnalysis.builder()
          .sourcePhrase(sourcePhrase)
          .analyzedTokens(List.of())
          .build();
    }
  }

  private FullAnalysis createAnalysis(AnalysisRequest event,
                                      AnalysisComponentRequest analysisComponentRequest) {
    var phrase = event.phrase();
    var tokens = tokenService.tokenize(phrase);
    analysisComponentRequest.setTokens(tokens);

    Optional<CompletableFuture<SemanticTranslation>> semanticTranslationFuture = Optional.empty();
    if (event.performSemanticTranslation()) {
      semanticTranslationFuture = Optional.of(supplyAsync(() ->
          semanticTranslationService.createAnalysisComponent(analysisComponentRequest)
      ));
    }

    if (tokens.size() < MAX_TOKENS) {
      var literalTranslationFuture = supplyAsync(() -> literalTranslationService.createAnalysisComponent(analysisComponentRequest));
      var grammaticalAnalysisFuture = supplyAsync(() -> morphologyService.createAnalysisComponent(analysisComponentRequest));
      awaitAll(literalTranslationFuture, grammaticalAnalysisFuture);

      var literalTranslation = join(literalTranslationFuture).getTokenTranslations();
      var grammaticalAnalysis = join(grammaticalAnalysisFuture);
      tokens = tokenService.enrichTokens(tokens, literalTranslation, grammaticalAnalysis);
    } else {
      log.info("Skipping literal translation and grammatical analysis for phrase '{}'", phrase);
      tokens = List.of();
    }

    var semanticTranslation = semanticTranslationFuture.map(CompletableFuture::join).orElse(null);

    return FullAnalysis.builder()
        .semanticTranslation(semanticTranslation)
        .sourcePhrase(phrase)
        .analyzedTokens(tokens)
        .build();
  }

  // So that a user can ask "How do I say ... in the language I'm learning?" and get a response
  // Creates a translation from the spoken language to the learned language, then creates a full analysis
  private FullAnalysis createReverseAnalysis(AnalysisRequest event, AnalysisComponentRequest request) {
    var learnedLanguageTranslation = semanticTranslationService.createAnalysisComponent(request);
    var learnedLanguageAnalysisEvent = createLearnedLanguageAnalysisEvent(learnedLanguageTranslation);
    var translation = SemanticTranslation.builder()
        .sourcePhrase(event.phrase())
        .translatedPhrase(learnedLanguageTranslation.getTranslatedPhrase())
        .build();
    request.reverseLanguages();
    request.setPhrase(learnedLanguageTranslation.getTranslatedPhrase());
    return createAnalysis(learnedLanguageAnalysisEvent, request.reverseLanguages()).toBuilder()
        .semanticTranslation(translation)
        .build();
  }

  private AnalysisRequest createLearnedLanguageAnalysisEvent(SemanticTranslation learnedLanguageTranslation) {
    return AnalysisRequest.builder()
        .phrase(learnedLanguageTranslation.getTranslatedPhrase())
        .performSemanticTranslation(false)
        .build();
  }

  public <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
    return CompletableFuture.supplyAsync(supplier, executorService);
  }

  public static void awaitAll(CompletableFuture<?>... futures) {
    CompletableFuture.allOf(futures);
  }

  public static <T> T join(CompletableFuture<T> future) {
    return future.join();
  }
}
