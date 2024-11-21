package com.grammr.language.translation.literal;

import com.grammr.domain.value.language.LiteralTranslation;
import com.grammr.language.Tokenizer;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenAILiteralTranslationService implements LiteralTranslationService {

  private final Tokenizer tokenizer;
  private final OpenAITokenTranslationService openAITokenTranslationService;

  @Override
  public LiteralTranslation createLiteralTranslation(String phrase) {
    var tokens = tokenizer.tokenize(phrase);

    var translatedTokens = tokens.stream()
        .map(token -> openAITokenTranslationService.createTranslatedToken(phrase, token))
        .collect(Collectors.toSet());

    log.info("Retrieved translated tokens: {}", translatedTokens);
    return new LiteralTranslation(phrase, translatedTokens);
  }
}
