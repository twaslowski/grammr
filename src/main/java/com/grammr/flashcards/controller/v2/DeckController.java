package com.grammr.flashcards.controller.v2;

import static com.grammr.domain.enums.ExportDataType.APKG;
import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.grammr.domain.entity.User;
import com.grammr.domain.enums.ExportDataType;
import com.grammr.flashcards.controller.v2.dto.DeckCreationDto;
import com.grammr.flashcards.controller.v2.dto.DeckDto;
import com.grammr.flashcards.controller.v2.dto.FlashcardDto;
import com.grammr.flashcards.controller.v2.dto.SyncResultDto;
import com.grammr.flashcards.service.DeckService;
import com.grammr.flashcards.service.FlashcardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
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
@Tag(name = "Decks", description = "Deck management operations")
@RequiredArgsConstructor
@RestController(value = "deckControllerV2")
@RequestMapping("/api/v2/deck")
public class DeckController {

  private static final ExportDataType EXPORT_DATA_TYPE = APKG;

  private final DeckService deckService;
  private final FlashcardService flashcardService;

  @Operation(summary = "Get all decks", description = "Get all decks for the authenticated user.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Decks retrieved successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthenticated"),
  })
  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<DeckDto>> getDecks(@AuthenticationPrincipal User user) {
    var decks = deckService.getDecks(user).stream()
        .map(DeckDto::from)
        .toList();
    return ResponseEntity.status(200).body(decks);
  }

  @Operation(summary = "Get existing deck", description = "Get existing deck for the authenticated user.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Deck found"),
      @ApiResponse(responseCode = "400", description = "deckId is not a valid UUID"),
      @ApiResponse(responseCode = "401", description = "Unauthenticated"),
      @ApiResponse(responseCode = "404", description = "Deck not found")
  })
  @GetMapping(path = "/{deckId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<DeckDto> getDeck(
      @AuthenticationPrincipal User user, @PathVariable UUID deckId
  ) {
    var deck = deckService.getDeck(deckId, user);
    return ResponseEntity.status(201).body(DeckDto.from(deck));
  }

  @Operation(summary = "Create a new deck", description = "Creates a new deck for the authenticated user.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Deck created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input"),
      @ApiResponse(responseCode = "401", description = "Unauthenticated"),
  })
  @PostMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<DeckDto> createDeck(
      @AuthenticationPrincipal User user,
      @Parameter(description = "Deck creation data") @RequestBody @Valid DeckCreationDto data) {
    var deck = deckService.createDeck(user, data.name(), data.description());
    var deckDto = DeckDto.from(deck);
    return ResponseEntity.status(201).body(deckDto);
  }

  @Operation(summary = "Export a deck", description = "Exports a deck in the APGK format")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Deck exported successfully"),
      @ApiResponse(responseCode = "400", description = "deckId is not a valid UUID"),
      @ApiResponse(responseCode = "404", description = "Deck not found")
  })
  @PostMapping(value = "/{deckId}/export")
  public ResponseEntity<byte[]> exportDeck(@AuthenticationPrincipal User user, @PathVariable UUID deckId) {
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
  public ResponseEntity<List<FlashcardDto>> syncDeck(
      @AuthenticationPrincipal User user,
      @PathVariable UUID deckId
  ) {
    var deck = deckService.getDeck(deckId, user);
    var flashcards = flashcardService.retrieveSyncableCards(deck);
    return ResponseEntity.ok(flashcards);
  }

  @Operation(
      summary = "Confirm sync operation",
      description = "Confirms that the flashcards have been synced to AnkiConnect from the frontend."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Sync confirmed"),
      @ApiResponse(responseCode = "400", description = "Malformed request, invalid UUID"),
  })
  @PostMapping(value = "/{deckId}/sync/confirm")
  public ResponseEntity<Void> confirmSync(
      @AuthenticationPrincipal User user,
      @PathVariable UUID deckId,
      @RequestBody SyncResultDto results
  ) {
    // Deck is required here for ownership check. Currently split into two queries
    // because ownership validation for Flashcards currently does not exist.
    var deck = deckService.getDeck(deckId, user);
    flashcardService.confirmSync(deck, results.successfulSyncs(), results.failedSyncs());
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Delete deck", description = "Deletes a deck by its ID")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Deck deleted successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthenticated"),
      @ApiResponse(responseCode = "404", description = "Deck not found")
  })
  @DeleteMapping(value = "/{deckId}")
  public ResponseEntity<Void> deleteDeck(@AuthenticationPrincipal User user, @PathVariable UUID deckId) {
    deckService.deleteDeck(deckId, user);
    return ResponseEntity.status(204).build();
  }

  @PostMapping(value = "/{deckId}/reset-sync")
  public ResponseEntity<Void> resetDeckSync(@AuthenticationPrincipal User user, @PathVariable UUID deckId) {
    var deck = deckService.getDeck(deckId, user);
    flashcardService.resetDeckSync(deck);
    return ResponseEntity.status(204).build();
  }
}
