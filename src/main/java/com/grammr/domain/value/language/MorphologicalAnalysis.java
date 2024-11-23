package com.grammr.domain.value.language;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Optional;
import lombok.Builder;

@Builder
public record MorphologicalAnalysis(
    @JsonProperty("source_phrase")
    String sourcePhrase,
    @JsonProperty("request_id")
    String requestId,
    List<TokenMorphology> tokens
) {

  public Optional<TokenMorphology> findByText(String token) {
    return tokens.stream()
        .filter(tokenMorphology -> tokenMorphology.text().equals(token))
        .findFirst();
  }
}
