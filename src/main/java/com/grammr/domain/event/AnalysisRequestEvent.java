package com.grammr.domain.event;

import com.grammr.domain.User;
import lombok.Builder;

@Builder
public record AnalysisRequestEvent(
    String phrase,
    String requestId,
    User user
) {

}
