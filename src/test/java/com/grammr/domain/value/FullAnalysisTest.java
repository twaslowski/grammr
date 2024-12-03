package com.grammr.domain.value;

import static org.assertj.core.api.Assertions.assertThat;

import com.grammr.domain.value.language.SemanticTranslation;
import com.grammr.domain.value.language.Token;
import com.grammr.domain.value.language.TokenTranslation;
import java.util.List;
import org.junit.jupiter.api.Test;

class FullAnalysisTest {

  @Test
  void shouldCalculateUsageTokens() {
    var semanticTranslation = SemanticTranslation.builder().build();

    semanticTranslation.setPromptTokens(10);
    semanticTranslation.setCompletionTokens(20);

    var tokenTranslation = TokenTranslation.builder().build();

    tokenTranslation.setPromptTokens(30);
    tokenTranslation.setCompletionTokens(40);

    var fullAnalysis = FullAnalysis.builder()
        .semanticTranslation(semanticTranslation)
        .analyzedTokens(List.of(Token.fromString("somestring")
            .withTranslation(tokenTranslation)))
        .build();

    assertThat(fullAnalysis.completionTokens()).isEqualTo(60);
    assertThat(fullAnalysis.promptTokens()).isEqualTo(40);
  }
}