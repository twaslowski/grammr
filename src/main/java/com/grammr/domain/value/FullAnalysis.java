package com.grammr.domain.value;

import com.grammr.domain.value.language.SemanticTranslation;
import com.grammr.domain.value.language.Token;
import com.grammr.domain.value.language.TokenTranslation;
import java.util.List;
import lombok.Builder;

@Builder(toBuilder = true)
public record FullAnalysis(
    String sourcePhrase,
    SemanticTranslation semanticTranslation,
    List<Token> analyzedTokens
) {

  public int completionTokens() {
    return semanticTranslation.getCompletionTokens()
        + analyzedTokens.stream()
        .map(Token::translation)
        .map(TokenTranslation::getCompletionTokens)
        .reduce(0, Integer::sum);
  }

  public int promptTokens() {
    return semanticTranslation.getPromptTokens()
        + analyzedTokens.stream()
        .map(Token::translation)
        .map(TokenTranslation::getPromptTokens)
        .reduce(0, Integer::sum);
  }
}
