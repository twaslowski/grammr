package com.grammr.port.rest.uri.local;

import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class MorphologyURIProvider implements com.grammr.port.rest.uri.MorphologyURIProvider {

  private final String host = "localhost";

  private final Map<String, String> ports = Map.of(
      "DE", "8001",
      "RU", "8002"
  );

  @Override
  public String provideUri(String languageCode) {
    return String.format("http://%s:%s/morphological-analysis", host, ports.get(languageCode));
  }
}
