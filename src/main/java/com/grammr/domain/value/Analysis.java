package com.grammr.domain.value;

import com.grammr.domain.value.language.LiteralTranslation;
import com.grammr.domain.value.language.SemanticTranslation;
import lombok.Builder;

@Builder
public record Analysis(
    String sourcePhrase,
    SemanticTranslation semanticTranslation,
    LiteralTranslation literalTranslation
) {

}