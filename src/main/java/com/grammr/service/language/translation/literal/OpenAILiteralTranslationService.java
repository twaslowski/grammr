package com.grammr.service.language.translation.literal;

import com.grammr.domain.value.language.TokenTranslation;
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

  private final OpenAITokenTranslationService openAITokenTranslationService;

  @Override
  public List<TokenTranslation> translateTokens(String phrase, List<String> words) {
    log.info("Translating {} unique words: {}", words.size(), words);
    var translationFutures = words.stream()
        .map(word -> openAITokenTranslationService.createTranslation(phrase, word))
        .toList();

    var translatedTokens = translationFutures.stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());

    log.info("Retrieved translated analyzedTokens: {}", translatedTokens);
    return translatedTokens;
  }
}
