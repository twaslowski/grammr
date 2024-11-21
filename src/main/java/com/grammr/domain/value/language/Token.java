package com.grammr.domain.value.language;

public record Token(
    String text,
    String translation,
    TokenMorphology morphology
) {

}
