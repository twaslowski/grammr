package com.grammr.config.value;

import static java.lang.String.format;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record InflectionConfiguration(
    boolean enabled,
    String host,
    int port
) {

  public String uri() {
    return format("http://%s:%d/inflect", host, port);
  }
}
