package com.grammr.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.entity.User;
import com.grammr.domain.enums.ExportDataType;
import com.grammr.port.dto.DeckDTO;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  public ResponseEntity<?> exportDeck(@RequestBody @Valid InboundAnkiDeckExportDto data, @AuthenticationPrincipal User user) {
    byte[] exportedDeck = ankiService.exportDeck(user, data.deckId(), data.exportDataType());
    var headers = new HttpHeaders();
    var filename = deriveFilename(data.deckId(), data.exportDataType());
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
    headers.add(HttpHeaders.ACCEPT, data.exportDataType().getMediaType());
    return ResponseEntity.ok().headers(headers).body(exportedDeck);
  }

  @PostMapping(value = "/sync")
  public ResponseEntity<DeckDTO> syncDeck(@AuthenticationPrincipal User user, @RequestBody InboundAnkiDeckExportDto dto) {
    var flashcards = ankiService.retrieveSyncableCards(dto.deckId());
    var deck = ankiService.getDeck(user, dto.deckId());
    DeckDTO response = DeckDTO.builder()
        .id(deck.getId())
        .name(deck.getName())
        .flashcards(flashcards)
        .build();
    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "/flashcard", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Flashcard> createFlashcard(@RequestBody @Valid AnkiFlashcardCreationDto data,
                                                   @AuthenticationPrincipal User user) {
    log.info("Creating flashcard for user {} in deck {}", user.getId(), data.deckId());
    var flashcard = ankiService.createFlashcard(user, data.deckId(), data.question(), data.answer(), data.tokenPos(), data.paradigmId());
    return ResponseEntity.status(201).body(flashcard);
  }

  @PostMapping(value = "/deck", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<DeckDTO> createDeck(@RequestBody @Valid AnkiDeckCreationDto data,
                                            @AuthenticationPrincipal User user) {
    var deck = ankiService.createDeck(user.getId(), data.name());
    var deckDto = new DeckDTO(deck, List.of());
    return ResponseEntity.status(201).body(deckDto);
  }

  @GetMapping(value = "/deck", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<DeckDTO>> getDecks(@AuthenticationPrincipal User user) {
    var decks = ankiService.getDecks(user.getId());
    return ResponseEntity.status(200).body(decks);
  }

  @GetMapping(value = "/deck/{deckId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<DeckDTO> getDeck(@AuthenticationPrincipal User user, @PathVariable long deckId) {
    var deck = ankiService.getDeck(user, deckId);
    var flashcards = ankiService.getFlashcards(deck.getId());
    return ResponseEntity.status(200).body(new DeckDTO(deck, flashcards));
  }

  @DeleteMapping(value = "/deck/{deckId}")
  public ResponseEntity<?> deleteDeck(@AuthenticationPrincipal User user, @PathVariable long deckId) {
    ankiService.deleteDeck(user, deckId);
    return ResponseEntity.status(204).build();
  }

  @DeleteMapping(value = "/flashcard/{flashcardId}")
  public ResponseEntity<?> deleteFlashcard(@AuthenticationPrincipal User user, @PathVariable long flashcardId) {
    ankiService.deleteFlashcard(user, flashcardId);
    return ResponseEntity.status(204).build();
  }

  private String deriveFilename(long deckId, ExportDataType exportDataType) {
    return "deck-" + deckId + "." + exportDataType.getExtension();
  }
}
