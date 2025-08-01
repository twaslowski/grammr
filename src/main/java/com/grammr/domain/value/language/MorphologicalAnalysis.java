package com.grammr.domain.value.language;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MorphologicalAnalysis {

  private String sourcePhrase;

  private List<TokenMorphology> tokens;

  public Optional<TokenMorphology> findByText(String token) {
    return tokens.stream()
        .filter(tokenMorphology -> tokenMorphology.text().equals(token))
        .findFirst();
  }
}
