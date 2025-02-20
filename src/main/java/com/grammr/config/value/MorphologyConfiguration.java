package com.grammr.config.value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record MorphologyConfiguration(
    boolean enabled,
    String host,
    int port
) {

  private static final String ENDPOINT = "/morphological-analysis";

  public String uri() {
    return "http://" + host + ":" + port + ENDPOINT;
  }
}
