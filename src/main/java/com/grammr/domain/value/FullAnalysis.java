package com.grammr.domain.value;

import com.grammr.domain.value.language.SemanticTranslation;
import com.grammr.domain.value.language.Token;
import com.grammr.domain.value.language.TokenTranslation;
import java.util.List;
import lombok.Builder;

@Builder
public record FullAnalysis(
    String sourcePhrase,
    SemanticTranslation semanticTranslation,
    List<Token> analyzedTokens
) {

  public long completionTokens() {
    return semanticTranslation.getCompletionTokens()
        + analyzedTokens.stream()
        .map(Token::translation)
        .map(TokenTranslation::getCompletionTokens)
        .reduce(0L, Long::sum);
  }

  public long promptTokens() {
    return semanticTranslation.getPromptTokens()
        + analyzedTokens.stream()
        .map(Token::translation)
        .map(TokenTranslation::getPromptTokens)
        .reduce(0L, Long::sum);
  }
}
