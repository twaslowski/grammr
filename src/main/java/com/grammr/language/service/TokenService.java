package com.grammr.language.service;

import com.grammr.domain.value.language.MorphologicalAnalysis;
import com.grammr.domain.value.language.Token;
import com.grammr.domain.value.language.TokenTranslation;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenService {

  public List<Token> tokenize(String phrase) {
    var indexCounter = new AtomicInteger(0);

    // Splits the phrase at boundaries between letters (\\p{L}) and punctuation (\\p{P}),
    // and ignores sequences of any characters which are neither letters nor punctuation.
    // Includes lookaheads and lookbehinds to ensure that punctuation is treated as separate tokens.
    return Arrays.stream(phrase.split("(?<=\\p{L})(?=\\p{P})|(?<=\\p{P})(?=\\p{L})|[^\\p{L}\\p{P}]+"))
        .filter(token -> !token.isEmpty())
        .map(token -> Token.fromString(token, indexCounter.getAndIncrement()))
        .filter(token -> !token.text().isBlank())
        .collect(Collectors.toList());
  }

  public List<Token> enrichTokens(List<Token> tokens,
                                  List<TokenTranslation> translations,
                                  MorphologicalAnalysis analysis) {
    return tokens.stream()
        .map(token -> enrichWithMorphology(token, analysis))
        .map(token -> enrichWithTranslation(token, translations))
        .sorted(Comparator.comparing(Token::index))
        .toList();
  }

  private Token enrichWithTranslation(Token token, List<TokenTranslation> literalTranslation) {
    return literalTranslation.stream().filter(translation -> translation.getIndex() == token.index())
        .findFirst()
        .map(token::withTranslation)
        .orElse(token);
  }

  private Token enrichWithMorphology(Token token, MorphologicalAnalysis analysis) {
    return analysis.findByText(token.text())
        .map(token::withMorphology)
        .orElse(token);
  }
}
