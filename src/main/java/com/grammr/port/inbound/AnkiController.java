package com.grammr.port.inbound;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.grammr.domain.entity.Deck;
import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.entity.User;
import com.grammr.domain.enums.ExportDataType;
import com.grammr.port.dto.anki.AnkiDeckCreationDto;
import com.grammr.port.dto.anki.AnkiFlashcardCreationDto;
import com.grammr.port.dto.anki.InboundAnkiDeckExportDto;
import com.grammr.service.AnkiService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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

  @PostMapping(value = "/export")
  public ResponseEntity<?> exportDeck(@RequestBody @Valid InboundAnkiDeckExportDto data) {
    byte[] exportedDeck = ankiService.exportDeck(data.deckId(), data.exportDataType());
    var headers = new HttpHeaders();
    var filename = deriveFilename(data.deckId(), data.exportDataType());
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
    headers.add(HttpHeaders.ACCEPT, data.exportDataType().getMediaType());
    return ResponseEntity.ok().headers(headers).body(exportedDeck);
  }

  @PostMapping(value = "/flashcard", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Flashcard> createFlashcard(@RequestBody @Valid AnkiFlashcardCreationDto data,
                                                   @AuthenticationPrincipal User user) {
    log.info("Creating flashcard for user {} in deck {}", user.getId(), data.deckId());
    var flashcard = ankiService.createFlashcard(data.deckId(), data.question(), data.answer());
    return ResponseEntity.status(201).body(flashcard);
  }

  @PostMapping(value = "/deck", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Deck> createDeck(@RequestBody @Valid AnkiDeckCreationDto data,
                                         @AuthenticationPrincipal User user) {
    var deck = ankiService.createDeck(user.getId(), data.name());
    return ResponseEntity.status(201).body(deck);
  }

  @GetMapping(value = "/decks", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Deck>> getDecks(@AuthenticationPrincipal User user) {
    var decks = ankiService.getDecks(user.getId());
    return ResponseEntity.status(200).body(decks);
  }

  private String deriveFilename(long deckId, ExportDataType exportDataType) {
    return "deck-" + deckId + "." + exportDataType.getExtension();
  }
}
