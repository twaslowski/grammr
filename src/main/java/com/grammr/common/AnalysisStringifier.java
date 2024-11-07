package com.grammr.common;

import static java.lang.String.format;
import static java.lang.String.join;

import com.grammr.domain.value.Analysis;
import com.grammr.domain.value.language.LiteralTranslation;
import com.grammr.domain.value.language.SemanticTranslation;

public class AnalysisStringifier {

  public static String stringifyAnalysis(Analysis analysis) {
    return format("%s%n%s",
        stringifySemanticTranslation(analysis.semanticTranslation()),
        stringifyLiteralTranslation(analysis.literalTranslation())
    );
  }

  public static String stringifySemanticTranslation(SemanticTranslation translation) {
    return format("%s translates to %s",
        bold(translation.sourcePhrase()),
        bold(translation.translatedPhrase()));
  }

  public static String stringifyLiteralTranslation(LiteralTranslation literalTranslation) {
    return join("\n", literalTranslation.translatedTokens().stream()
        .map(token -> format("%s -> %s",
            italic(token.source()),
            italic(token.translation())))
        .toList());
  }

  private static String bold(String text) {
    return format("'<b>%s</b>'", text);
  }

  private static String italic(String text) {
    return format("'<i>%s</i>'", text);
  }
}
