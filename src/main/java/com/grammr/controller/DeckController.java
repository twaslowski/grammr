package com.grammr.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.grammr.controller.dto.DeckCreationDto;
import com.grammr.controller.dto.DeckDto;
import com.grammr.controller.dto.DeckExportDto;
import com.grammr.domain.entity.Deck;
import com.grammr.domain.entity.User;
import com.grammr.domain.enums.ExportDataType;
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
@RequestMapping("/api/v1/deck")
public class DeckController {

  private final AnkiService ankiService;

  @PostMapping(value = "/export")
  public ResponseEntity<?> exportDeck(@RequestBody @Valid DeckExportDto data, @AuthenticationPrincipal User user) {
    var deck = ankiService.getDeck(user, data.deckId());
    byte[] exportedDeck = ankiService.exportDeck(deck, data.exportDataType());
    var headers = new HttpHeaders();
    var filename = deriveFilename(deck, data.exportDataType());
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
    headers.add(HttpHeaders.ACCEPT, data.exportDataType().getMediaType());
    return ResponseEntity.ok().headers(headers).body(exportedDeck);
  }

  @PostMapping(value = "/sync")
  public ResponseEntity<DeckDto> syncDeck(@AuthenticationPrincipal User user, @RequestBody DeckExportDto dto) {
    var flashcards = ankiService.retrieveSyncableCards(dto.deckId());
    var deck = ankiService.getDeck(user, dto.deckId());
    DeckDto response = DeckDto.builder()
        .id(deck.getId())
        .name(deck.getName())
        .flashcards(flashcards)
        .build();
    return ResponseEntity.ok(response);
  }

  @PostMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<DeckDto> createDeck(@RequestBody @Valid DeckCreationDto data,
                                            @AuthenticationPrincipal User user) {
    var deck = ankiService.createDeck(user.getId(), data.name());
    var deckDto = new DeckDto(deck, List.of());
    return ResponseEntity.status(201).body(deckDto);
  }

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<DeckDto>> getDecks(@AuthenticationPrincipal User user) {
    var decks = ankiService.getDecks(user.getId());
    return ResponseEntity.status(200).body(decks);
  }

  @GetMapping(value = "/{deckId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<DeckDto> getDeck(@AuthenticationPrincipal User user, @PathVariable long deckId) {
    var deck = ankiService.getDeck(user, deckId);
    var flashcards = ankiService.getFlashcards(deck.getId());
    return ResponseEntity.status(200).body(new DeckDto(deck, flashcards));
  }

  @DeleteMapping(value = "/{deckId}")
  public ResponseEntity<?> deleteDeck(@AuthenticationPrincipal User user, @PathVariable long deckId) {
    var deck = ankiService.getDeck(user, deckId);
    ankiService.deleteDeck(user, deckId);
    return ResponseEntity.status(204).build();
  }

  private String deriveFilename(Deck deck, ExportDataType exportDataType) {
    return deck.getName() + exportDataType.getExtension();
  }
}
