package com.grammr.domain.value;

import com.grammr.domain.entity.Request;
import java.time.temporal.ChronoUnit;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
public record RequestStats(
    int promptTokens,
    int completionTokens,
    long requestTime
) {

  public static RequestStats from(Request request) {
    var requestTime = ChronoUnit.MILLIS.between(request.getCreatedTimestamp(), request.getUpdatedTimestamp());
    log.info("created timestamp: {}, updated: {}, diff: {}", request.getCreatedTimestamp(), request.getUpdatedTimestamp(), requestTime);
    return RequestStats.builder()
        .requestTime(requestTime)
        .completionTokens(request.getCompletionTokens())
        .promptTokens(request.getPromptTokens())
        .build();
  }

  @Override
  public String toString() {
    return String.format("RequestStats: promptTokens=%d, completionTokens=%d, elapsed time=%,.2fs",
        promptTokens, completionTokens, requestTime / 1000.0);
  }
}

