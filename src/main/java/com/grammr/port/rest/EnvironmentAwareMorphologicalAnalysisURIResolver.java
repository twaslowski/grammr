package com.grammr.port.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnvironmentAwareMorphologicalAnalysisURIResolver {

  // This is a terribly ugly hack until I finally switch to a Message Broker.
  // Subdomain example: de.localhost:8000/analysis
  // Path example:      localhost:8000/de/analysis
  public enum Mechanism {
    PATH,
    PREFIX
  }

  @Value("${analysis.rest.uri-resolution.mechanism}")
  private Mechanism mechanism;

  @Value("${analysis.rest.host}")
  private String host;

  @Value("${analysis.rest.port}")
  private String port;

  @Value("${analysis.rest.endpoint}")
  private String endpoint;

  public String resolveUri(String languageCode) {
    return switch (mechanism) {
      case PATH -> buildPathUri(languageCode);
      case PREFIX -> buildPrefixedUri(languageCode);
    };
  }

  private String buildPathUri(String languageCode) {
    return String.format("http://%s:%s/%s/%s", host, port, languageCode.toLowerCase(), endpoint);
  }

  private String buildPrefixedUri(String languageCode) {
    return String.format("http://%s-%s:%s/%s", languageCode.toLowerCase(), host, port, endpoint);
  }
}
