package com.grammr.common;

import static java.lang.String.format;
import static java.lang.String.join;

import com.grammr.domain.enums.features.FeatureCategory;
import com.grammr.domain.enums.features.FeatureType;
import com.grammr.domain.value.FullAnalysis;
import com.grammr.domain.value.language.SemanticTranslation;
import com.grammr.domain.value.language.Token;
import com.grammr.domain.value.language.TokenMorphology;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FullAnalysisStringificationService {

  private static final String EMPTY_STRING = "";
  private static final String DELIMITER = ",";
  private static final String NEWLINE = "\n";

  public String stringifyAnalysis(FullAnalysis fullAnalysis) {
    return format("%s%s%s%s",
        stringifySemanticTranslation(fullAnalysis.semanticTranslation()),
        NEWLINE,
        NEWLINE,
        stringifyTokens(fullAnalysis.analyzedTokens()));
  }

  public String stringifySemanticTranslation(SemanticTranslation translation) {
    return format("%s translates to %s",
        bold(translation.getSourcePhrase()),
        bold(translation.getTranslatedPhrase()));
  }

  public String stringifyTokens(List<Token> tokens) {
    return join(NEWLINE, tokens.stream()
        .map(this::stringifyToken)
        .toList());
  }

  private String stringifyToken(Token token) {
    StringBuilder stringBuilder = new StringBuilder();
    if (token.translation() != null) {
      stringBuilder.append(stringifyTokenTranslation(token));
    }
    if (token.morphology() != null) {
      stringBuilder.append(stringifyTokenMorphology(token.morphology()));
    }
    return stringBuilder.toString();
  }

  private String stringifyTokenMorphology(TokenMorphology tokenMorphology) {
    StringBuilder stringBuilder = new StringBuilder();
    if (tokenMorphology.text().equalsIgnoreCase(tokenMorphology.lemma())) {
      return stringBuilder.toString();
    }
    stringBuilder.append(String.format("%s (from %s)", DELIMITER, italic(tokenMorphology.lemma())));
    if (tokenMorphology.partOfSpeechTag() != null) {
      stringBuilder.append(String.format("%s %s", DELIMITER, tokenMorphology.partOfSpeechTag().getFullIdentifier()));
    }
    if (shouldStringifyFeatures(tokenMorphology)) {
      stringBuilder.append(String.format("%s %s", DELIMITER, stringifyFeatures(tokenMorphology)).toLowerCase());
    }

    return stringBuilder.toString();
  }

  private static boolean shouldStringifyFeatures(TokenMorphology tokenMorphology) {
    return tokenMorphology.partOfSpeechTag() != null && tokenMorphology.partOfSpeechTag().getFeatureCategory() != FeatureCategory.OTHER;
  }

  private String stringifyFeatures(TokenMorphology tokenMorphology) {
    return switch (tokenMorphology.partOfSpeechTag().getFeatureCategory()) {
      case VERBAL -> stringifyVerbalFeatures(tokenMorphology);
      case NOMINAL -> stringifyNominalFeatures(tokenMorphology);
      default -> EMPTY_STRING;
    };
  }

  private String stringifyVerbalFeatures(TokenMorphology morphology) {
    return format("%s %s %s",
        morphology.getFullFeatureIdentifier(FeatureType.PERSON),
        morphology.getFullFeatureIdentifier(FeatureType.NUMBER),
        morphology.getFullFeatureIdentifier(FeatureType.TENSE));
  }

  private String stringifyNominalFeatures(TokenMorphology morphology) {
    return format("%s %s %s",
        morphology.getFullFeatureIdentifier(FeatureType.CASE),
        morphology.getFullFeatureIdentifier(FeatureType.NUMBER),
        morphology.getFullFeatureIdentifier(FeatureType.GENDER));
  }

  private String stringifyTokenTranslation(Token token) {
    return format("%s -> %s", bold(token.text()), token.translation().getTranslation());
  }

  private String bold(String text) {
    return format("<b>%s</b>", text);
  }

  private String italic(String text) {
    return format("<i>%s</i>", text);
  }
}
