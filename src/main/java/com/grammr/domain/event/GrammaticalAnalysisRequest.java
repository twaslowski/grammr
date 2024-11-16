package com.grammr.domain.event;

public record GrammaticalAnalysisRequest(
    String phrase,
    String requestId
) {

}
