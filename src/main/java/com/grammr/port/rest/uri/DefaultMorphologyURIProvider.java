package com.grammr.port.rest.uri;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile("prod")
public class DefaultMorphologyURIProvider implements MorphologyURIProvider {

  @Value("${analysis.rest.host}")
  private String host;

  @Value("${analysis.rest.port}")
  private String port;

  @Override
  public String provideUri(String languageCode) {
    return String.format("http://%s.%s:%s/morphological-analysis", languageCode.toLowerCase(), host, port);
  }
}
