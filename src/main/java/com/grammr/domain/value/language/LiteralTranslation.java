package com.grammr.domain.value.language;

import java.util.Collection;
import java.util.Optional;
import lombok.Builder;

@Builder
public record LiteralTranslation(
    String sourcePhrase,
    Collection<TokenTranslation> tokenTranslations
) {

  public Optional<TokenTranslation> findBySourceText(String text) {
    return tokenTranslations.stream()
        .filter(tokenTranslation -> tokenTranslation.source().equals(text))
        .findFirst();
  }
}
