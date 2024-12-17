package com.grammr.service.language.translation.literal;

import com.grammr.domain.value.AnalysisComponentRequest;
import com.grammr.domain.value.language.LiteralTranslation;
import com.grammr.domain.value.language.Token;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenAILiteralTranslationService implements LiteralTranslationService {

  private final OpenAITokenTranslationService openAITokenTranslationService;

  @Override
  public LiteralTranslation createAnalysisComponent(AnalysisComponentRequest request) {
    var words = request.getTokens().stream()
        .map(Token::text)
        .map(String::toLowerCase)
        .distinct()
        .toList();

    log.info("Translating {} unique words: {}", words.size(), words);
    var translationFutures = words.stream()
        .map(word -> openAITokenTranslationService.createTranslation(request.getPhrase(), word))
        .toList();

    var translatedTokens = translationFutures.stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());

    log.info("Retrieved translated analyzedTokens: {}", translatedTokens);
    return LiteralTranslation.builder()
        .sourcePhrase(request.getPhrase())
        .tokenTranslations(translatedTokens)
        .build();
  }
}
