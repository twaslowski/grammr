package com.grammr.port.rest;

import static java.lang.String.format;

import com.grammr.domain.value.language.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class InflectionGeneratorPort {

  private static final String URL = "http://localhost:8010/api/inflect";

  private final RestClient restClient;

  public String generateInflections(Token token) {
    var uri = buildUri(token);
    return restClient.get()
        .uri(uri)
        .retrieve()
        .body(String.class);
  }

  public String buildUri(Token token) {
    StringBuilder stringBuilder = new StringBuilder(URL);
    stringBuilder.append(format("?phrase=%s", token.lemma()));
    stringBuilder.append("&forms=gent");
    return stringBuilder.toString();
  }
}
