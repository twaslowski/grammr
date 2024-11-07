package com.grammr.domain.event;

import com.grammr.domain.value.Analysis;
import lombok.Builder;

@Builder
public record AnalysisCompleteEvent(
    Analysis analysis,
    String requestId,
    long chatId
) {

}
