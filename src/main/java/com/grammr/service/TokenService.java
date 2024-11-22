package com.grammr.service;

import com.grammr.domain.value.language.LiteralTranslation;
import com.grammr.domain.value.language.MorphologicalAnalysis;
import com.grammr.domain.value.language.Token;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenService {

  public List<String> tokenize(String phrase) {
    return Arrays.stream(phrase.split("\\P{L}+"))
        .filter(token -> !token.isEmpty())
        .collect(Collectors.toList());
  }

  public List<Token> consolidateTokens(List<String> tokens,
                                       MorphologicalAnalysis analysis,
                                       LiteralTranslation literalTranslation) {
    return tokens.stream()
        .map(Token::create)
        .map(token -> enrichWithMorphology(token, analysis))
        .map(token -> enrichWithTranslation(token, literalTranslation))
        .toList();
  }

  private Token enrichWithTranslation(Token token, LiteralTranslation literalTranslation) {
    return literalTranslation.findBySourceText(token.text())
        .map(token::withTranslation)
        .orElse(token);
  }

  private Token enrichWithMorphology(Token token, MorphologicalAnalysis analysis) {
    return analysis.findByText(token.text())
        .map(token::withMorphology)
        .orElse(token);
  }
}
