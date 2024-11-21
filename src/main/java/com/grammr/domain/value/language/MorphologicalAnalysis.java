package com.grammr.domain.value.language;

import java.util.List;

public record MorphologicalAnalysis(
    String sourcePhrase,
    String requestId,
    List<TokenMorphology> tokens
) {

}
