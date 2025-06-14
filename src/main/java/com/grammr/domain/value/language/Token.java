package com.grammr.domain.value.language;

import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.value.language.v2.WordTranslation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record Token(
    @NotNull
    @Schema(
        description = "The index of the token in the original text",
        example = "0",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    long index,
    @NotNull
    @Schema(
        description = "The text of the token",
        example = "Hello",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String text,
    @Nullable
    @Schema(
        description = "The translation of the token, if available",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    WordTranslation translation,
    @NotNull
    @Schema(
        description = "The morphology of the token, including lemma and part of speech tag",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    TokenMorphology morphology
) {

  public static Token fromString(String text, long index) {
    return new Token(index, text, null, null);
  }

  public String lemma() {
    return morphology.lemma();
  }

  public PartOfSpeechTag partOfSpeechTag() {
    return morphology.partOfSpeechTag();
  }

  public Token withMorphology(TokenMorphology morphology) {
    return new Token(index, text, translation, morphology);
  }
}
