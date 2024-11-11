package com.grammr.domain.event;

import lombok.Builder;

@Builder
public record FullAnalysisRequest(
    String phrase,
    String requestId,
    // Todo: Most likely, the Telegram Port should eventually have its own database
    // to link the unique identifier to a Telegram Chat; potentially alongside the capacity
    // to edit temporary messages. For now, this leaky abstraction will do.
    long chatId
) {

}
