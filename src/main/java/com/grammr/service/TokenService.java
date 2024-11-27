package com.grammr.service;

import com.grammr.domain.value.language.MorphologicalAnalysis;
import com.grammr.domain.value.language.Token;
import com.grammr.domain.value.language.TokenTranslation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenService {

  public List<Token> tokenize(String phrase) {
    return Arrays.stream(phrase.split("\\P{L}+"))
        .filter(token -> !token.isEmpty())
        .map(Token::fromString)
        .collect(Collectors.toList());
  }

  public List<Token> enrichTokens(List<Token> tokens,
                                  List<TokenTranslation> translations,
                                  MorphologicalAnalysis analysis) {
    return tokens.stream()
        .map(token -> enrichWithMorphology(token, analysis))
        .map(token -> enrichWithTranslation(token, translations))
        .toList();
  }

  private Token enrichWithTranslation(Token token, List<TokenTranslation> literalTranslation) {
    return literalTranslation.stream().filter(translation -> translation.source().equals(token.text()))
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
