package com.grammr.domain.value.language;

import com.grammr.domain.value.AnalysisComponent;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MorphologicalAnalysis extends AnalysisComponent {

  private String sourcePhrase;

  private List<TokenMorphology> tokens;

  public Optional<TokenMorphology> findByText(String token) {
    return tokens.stream()
        .filter(tokenMorphology -> tokenMorphology.text().equals(token))
        .findFirst();
  }
}
