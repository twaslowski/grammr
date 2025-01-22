package com.grammr.config.value;

import static java.lang.String.format;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MorphologyConfiguration(
    boolean enabled,
    String host,
    int port
) {

  public String uri() {
    return format("http://%s:%d/morphological-analysis", host, port);
  }
}
