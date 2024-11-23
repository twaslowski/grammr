package com.grammr.domain.value.language;

import lombok.Builder;

@Builder
public record SemanticTranslation(
    String sourcePhrase,
    String translatedPhrase
) {

}
