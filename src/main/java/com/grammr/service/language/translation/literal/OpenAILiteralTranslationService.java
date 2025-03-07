package com.grammr.service.language.translation.literal;

import com.grammr.domain.value.AnalysisComponentRequest;
import com.grammr.domain.value.language.LiteralTranslation;
import com.grammr.domain.value.language.Token;
import io.micrometer.core.annotation.Timed;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenAILiteralTranslationService implements LiteralTranslationService {

  private static final int MAX_TOKENS = 15;

  private final OpenAITokenTranslationService openAITokenTranslationService;

  @Override
  @Timed("analysis.literal_translation")
  public LiteralTranslation createAnalysisComponent(AnalysisComponentRequest request) {
    if (request.getTokens().size() > MAX_TOKENS) {
      return LiteralTranslation.builder()
          .sourcePhrase(request.getPhrase())
          .tokenTranslations(List.of())
          .build();
    }

    log.info("Performing translations for {} tokens", request.getTokens().size());

    var completableFutures = request.getTokens().stream()
        .map(token -> CompletableFuture.supplyAsync(() -> openAITokenTranslationService.createTranslation(request.getPhrase(), token, request.getTargetLanguage())))
        .toList();

    var tokenTranslations = completableFutures.stream()
        .parallel()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());

    return LiteralTranslation.builder()
        .sourcePhrase(request.getPhrase())
        .tokenTranslations(tokenTranslations)
        .build();
  }
}
