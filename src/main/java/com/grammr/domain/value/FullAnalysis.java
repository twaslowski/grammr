package com.grammr.domain.value;

import com.grammr.domain.value.language.SemanticTranslation;
import com.grammr.domain.value.language.Token;
import com.grammr.domain.value.language.TokenTranslation;
import java.util.List;
import java.util.Optional;
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
        .map(translation -> Optional.ofNullable(translation)
            .map(TokenTranslation::getCompletionTokens)
            .orElse(0))
        .reduce(0, Integer::sum);
  }

  public int promptTokens() {
    return semanticTranslation.getPromptTokens()
        + analyzedTokens.stream()
        .map(Token::translation)
        .map(translation -> Optional.ofNullable(translation)
            .map(TokenTranslation::getPromptTokens)
            .orElse(0))
        .reduce(0, Integer::sum);
  }
}
