package com.grammr.port;

import com.grammr.domain.entity.Deck;
import com.grammr.domain.entity.Flashcard;
import com.grammr.port.dto.outbound.anki.OutboundAnkiDeckExportDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnkiPort {

  @Value("${grammr.anki.api.host}")
  private String host;

  @Value("${grammr.anki.api.endpoint}")
  private String endpoint;

  private final RestClient restClient;

  public byte[] exportDeck(Deck deck, List<Flashcard> flashcards) {
    var uri = constructUri();
    var requestBody = new OutboundAnkiDeckExportDto(deck.getId(), deck.getName(), flashcards);
    try {
      return restClient
          .post()
          .uri(uri)
          .body(requestBody)
          .retrieve()
          .body(byte[].class);
    } catch (Exception e) {
      log.error("Error exporting deck with id: {}", deck.getId(), e);
      throw e;
    }
  }

  private String constructUri() {
    return String.format("%s/%s", host, endpoint);
  }
}
