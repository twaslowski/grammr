package com.grammr.domain.value.language;

import java.util.List;

public record GrammaticalAnalysis(
    String sourcePhrase,
    String requestId,
    List<GrammaticalAnalysisToken> tokens
) {

}
