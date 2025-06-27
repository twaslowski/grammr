package com.grammr.flashcards.controller.v2;

import static com.grammr.domain.enums.ExportDataType.APKG;
import static java.lang.String.format;

import com.grammr.domain.entity.User;
import com.grammr.domain.enums.ExportDataType;
import com.grammr.flashcards.controller.dto.DeckDto;
import com.grammr.flashcards.service.DeckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Decks", description = "Deck management operations")
@RequiredArgsConstructor
@RestController(value = "deckControllerV2")
@RequestMapping("/api/v2/deck")
public class DeckController {

  private static final ExportDataType EXPORT_DATA_TYPE = APKG;

  private final DeckService deckService;

  @Operation(summary = "Export a deck", description = "Exports a deck in the APGK format")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Deck exported successfully"),
      @ApiResponse(responseCode = "400", description = "deckId is not a valid UUID"),
      @ApiResponse(responseCode = "404", description = "Deck not found")
  })
  @PostMapping(value = "/{deckId}/export")
  public ResponseEntity<?> exportDeck(@AuthenticationPrincipal User user, @PathVariable UUID deckId) {
    var deck = deckService.getDeck(deckId, user);
    byte[] exportedDeck = deckService.exportDeck(deck, EXPORT_DATA_TYPE);
    var filename = format("%s.%s", deck.getName(), EXPORT_DATA_TYPE);

    var headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
    headers.add(HttpHeaders.ACCEPT, APKG.getMediaType());

    return ResponseEntity.ok().headers(headers).body(exportedDeck);
  }

  @Operation(summary = "Sync deck to AnkiConnect", description = """
      Returns a list of flashcards to sync to AnkiConnect.
      The sync status of flashcards is internally maintained, and a sync operation
      has to be confirmed afterwards.
      """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Flashcard retrieval successful"),
      @ApiResponse(responseCode = "400", description = "deckId is not a valid UUID"),
      @ApiResponse(responseCode = "404", description = "Deck not found")
  })
  @PostMapping(value = "/{deckId}/sync")
  public ResponseEntity<DeckDto> syncDeck(
      @AuthenticationPrincipal User user, @PathVariable UUID deckId) {
    // var flashcards = flashcardService.retrieveSyncableCards(dto.deckId());
    var deck = deckService.getDeck(deckId, user);
    DeckDto response = DeckDto.from(deck);
    return ResponseEntity.ok(response);
  }
}
