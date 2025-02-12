package com.grammr.port.inbound;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

import com.grammr.domain.entity.Deck;
import com.grammr.domain.entity.Flashcard;
import com.grammr.port.dto.anki.AnkiDeckCreationDto;
import com.grammr.port.dto.anki.AnkiFlashcardCreationDto;
import com.grammr.port.dto.anki.InboundAnkiDeckExportDto;
import com.grammr.service.AnkiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/anki")
public class AnkiController {

  private final AnkiService ankiService;

  @PostMapping(value = "/export", produces = APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<byte[]> exportDeck(@RequestBody InboundAnkiDeckExportDto data) {
    byte[] exportedDeck = ankiService.exportDeck(data.userId());
    var headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=deck.apkg");
    return ResponseEntity.ok().headers(headers).body(exportedDeck);
  }

  @PostMapping(value = "/flashcard", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Flashcard> createFlashcard(@RequestBody AnkiFlashcardCreationDto data) {
    var flashcard = ankiService.createFlashcard(data.deckId(), data.question(), data.answer());
    return ResponseEntity.status(201).body(flashcard);
  }

  @PostMapping(value = "/deck", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Deck> createDeck(@RequestBody AnkiDeckCreationDto data) {
    var deck = ankiService.createDeck(data.userId(), data.name());
    return ResponseEntity.status(201).body(deck);
  }
}
