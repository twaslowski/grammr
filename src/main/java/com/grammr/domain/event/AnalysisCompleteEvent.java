package com.grammr.domain.event;

import com.grammr.domain.value.FullAnalysis;
import lombok.Builder;

@Builder
public record AnalysisCompleteEvent(
    String requestId,
    long chatId,
    FullAnalysis fullAnalysis
) {

}
