package com.grammr.service;

import com.grammr.domain.event.AnalysisRequest;
import com.grammr.domain.value.AnalysisComponentRequest;
import com.grammr.domain.value.FullAnalysis;
import com.grammr.service.language.morphology.MorphologyService;
import com.grammr.service.language.recognition.LanguageRecognitionService;
import com.grammr.service.language.translation.literal.OpenAILiteralTranslationService;
import com.grammr.service.language.translation.semantic.SemanticTranslationService;
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

  private final ExecutorService executorService = Executors.newCachedThreadPool();
  private final SemanticTranslationService semanticTranslationService;
  private final LanguageRecognitionService languageRecognitionService;
  private final OpenAILiteralTranslationService literalTranslationService;
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
        .sourceLanguage(analysisRequest.userLanguageLearned())
        .targetLanguage(analysisRequest.userLanguageSpoken())
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
        .sourceLanguage(analysisRequest.userLanguageLearned())
        .targetLanguage(analysisRequest.userLanguageSpoken())
        .semanticTranslation(semanticTranslation)
        .analyzedTokens(tokens)
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
