package com.grammr.service.language.translation.literal;

import com.grammr.domain.value.language.TokenTranslation;
import java.util.List;
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
    var translatedTokens = words.stream()
        .map(word -> openAITokenTranslationService.createTranslation(phrase, word))
        .collect(Collectors.toList());

    log.info("Retrieved translated tokens: {}", translatedTokens);
    return translatedTokens;
  }
}
