package com.grammr.domain.value.language;

import java.util.List;
import java.util.Optional;
import lombok.Builder;

@Builder
public record MorphologicalAnalysis(
    String sourcePhrase,
    String requestId,
    List<TokenMorphology> tokens
) {

  public Optional<TokenMorphology> findByText(String token) {
    return tokens.stream()
        .filter(tokenMorphology -> tokenMorphology.text().equals(token))
        .findFirst();
  }
}
