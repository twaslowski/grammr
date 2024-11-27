package com.grammr.domain.event;

import com.grammr.domain.value.FullAnalysis;
import lombok.Builder;

@Builder
public record AnalysisCompleteEvent(
    FullAnalysis fullAnalysis,
    String requestId,
    long chatId
) {

}
