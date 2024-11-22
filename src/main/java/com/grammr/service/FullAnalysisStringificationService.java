package com.grammr.service;

import static java.lang.String.format;
import static java.lang.String.join;

import com.grammr.domain.value.FullAnalysis;
import com.grammr.domain.value.language.SemanticTranslation;
import com.grammr.domain.value.language.Token;
import com.grammr.domain.value.language.TokenMorphology;
import java.util.List;

public class FullAnalysisStringificationService {

  public static String stringifyAnalysis(FullAnalysis fullAnalysis) {
    return format("%s%n%s",
        stringifySemanticTranslation(fullAnalysis.semanticTranslation()),
        stringifyTokens(fullAnalysis.tokens())
    );
  }

  public static String stringifySemanticTranslation(SemanticTranslation translation) {
    return format("%s translates to %s",
        bold(translation.sourcePhrase()),
        bold(translation.translatedPhrase()));
  }

  public static String stringifyTokens(List<Token> tokens) {
    return join("\n", tokens.stream()
        .map(FullAnalysisStringificationService::stringifyToken)
        .toList());
  }

  private static String stringifyToken(Token token) {
    StringBuilder stringBuilder = new StringBuilder(bold(token.text()));
    if (token.translation() != null) {
      stringBuilder.append(stringifyTokenTranslation(token));
    }
    if (token.morphology() != null) {
      stringBuilder.append(stringifyTokenMorphology(token.morphology()));
    }
    return stringBuilder.toString();
  }

  private static String stringifyTokenMorphology(TokenMorphology tokenMorphology) {
    if (!tokenMorphology.text().equalsIgnoreCase(tokenMorphology.lemma())) {
      return "";
    }
    return String.format("(from %s)", italic(tokenMorphology.lemma()));
  }

  private static String stringifyTokenTranslation(Token token) {
    return format("%s -> %s", bold(token.text()), bold(token.translation()));
  }

  private static String bold(String text) {
    return format("'<b>%s</b>'", text);
  }

  private static String italic(String text) {
    return format("'<i>%s</i>'", text);
  }
}
