package com.grammr.domain.value;

import com.grammr.domain.entity.Request;
import java.time.temporal.ChronoUnit;
import lombok.Builder;

@Builder
public record RequestStats(
    int promptTokens,
    int completionTokens,
    long requestTime
) {

  public static RequestStats from(Request request) {
    var requestTime = ChronoUnit.MILLIS.between(request.getCreatedTimestamp(), request.getUpdatedTimestamp());
    return RequestStats.builder()
        .requestTime(requestTime)
        .completionTokens(request.getCompletionTokens())
        .promptTokens(request.getPromptTokens())
        .build();
  }

  @Override
  public String toString() {
    return String.format("RequestStats: promptTokens=%d, completionTokens=%d, requestTime=%d seconds}",
        promptTokens, completionTokens, (requestTime / 1000));
  }
}

