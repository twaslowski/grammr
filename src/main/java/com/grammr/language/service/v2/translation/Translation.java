package com.grammr.language.service.v2.translation;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.language.Token;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.lang.Nullable;

public record Translation(
    @NotNull String source,
    @NotNull String translation,
    @NotNull LanguageCode sourceLanguage,
    @NotNull LanguageCode targetLanguage,
    @Nullable List<Token> analysedTokens
) {

  public Translation withTokens(List<Token> tokens) {
    return new Translation(
        source,
        translation,
        sourceLanguage,
        targetLanguage,
        tokens
    );
  }
}
