package com.grammr.flashcards.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.grammr.domain.entity.Deck;
import com.grammr.domain.entity.User;
import com.grammr.domain.enums.ExportDataType;
import com.grammr.flashcards.controller.dto.DeckCreationDto;
import com.grammr.flashcards.controller.dto.DeckDto;
import com.grammr.flashcards.controller.dto.DeckExportDto;
import com.grammr.flashcards.service.FlashcardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Decks", description = "Deck management operations")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/deck")
public class DeckController {

  private final FlashcardService flashcardService;

  @Operation(summary = "Create a new deck", description = "Creates a new deck for the authenticated user.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Deck created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input")
  })
  @PostMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<DeckDto> createDeck(
      @AuthenticationPrincipal User user,
      @Parameter(description = "Deck creation data") @RequestBody @Valid DeckCreationDto data) {
    var deck = flashcardService.createDeck(user.getId(), data.name());
    var deckDto = new DeckDto(deck, List.of());
    return ResponseEntity.status(201).body(deckDto);
  }

  @Operation(summary = "Export a deck", description = "Exports a deck in the specified format [APKG, DB]")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Deck exported successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input"),
      @ApiResponse(responseCode = "404", description = "Deck not found")
  })
  @PostMapping(value = "/export")
  public ResponseEntity<?> exportDeck(
      @Parameter(description = "Export data") @RequestBody @Valid DeckExportDto data,
      @AuthenticationPrincipal User user) {
    var deck = flashcardService.getDeck(user, data.deckId());
    byte[] exportedDeck = flashcardService.exportDeck(deck, data.exportDataType());
    var headers = new HttpHeaders();
    var filename = deriveFilename(deck, data.exportDataType());
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
    headers.add(HttpHeaders.ACCEPT, data.exportDataType().getMediaType());
    return ResponseEntity.ok().headers(headers).body(exportedDeck);
  }

  @Operation(summary = "Sync a deck", description = "Syncs flashcards for a deck.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Deck synced successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input"),
      @ApiResponse(responseCode = "404", description = "Deck not found")
  })
  @PostMapping(value = "/sync")
  public ResponseEntity<DeckDto> syncDeck(
      @AuthenticationPrincipal User user,
      @Parameter(description = "Deck export data") @RequestBody DeckExportDto dto) {
    var flashcards = flashcardService.retrieveSyncableCards(dto.deckId());
    var deck = flashcardService.getDeck(user, dto.deckId());
    DeckDto response = DeckDto.builder()
        .id(deck.getId())
        .name(deck.getName())
        .flashcards(flashcards)
        .build();
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Get decks", description = "Get all decks for the authenticated user.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Deck synced successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthenticated")
  })
  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<DeckDto>> getDecks(@AuthenticationPrincipal User user) {
    var decks = flashcardService.getDecks(user.getId());
    return ResponseEntity.status(200).body(decks);
  }

  @Operation(summary = "Get deck", description = "Get a specific deck of the authenticated user by ID.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Deck retrieved successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthenticated"),
      @ApiResponse(responseCode = "404", description = "Deck not found")
  })
  @GetMapping(value = "/{deckId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<DeckDto> getDeck(@AuthenticationPrincipal User user, @PathVariable long deckId) {
    var deck = flashcardService.getDeck(user, deckId);
    var flashcards = flashcardService.getFlashcards(deck.getId());
    return ResponseEntity.status(200).body(new DeckDto(deck, flashcards));
  }

  @Operation(summary = "Delete deck", description = "Deletes a deck by its ID")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Deck deleted successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthenticated"),
      @ApiResponse(responseCode = "404", description = "Deck not found")
  })
  @DeleteMapping(value = "/{deckId}")
  public ResponseEntity<?> deleteDeck(@AuthenticationPrincipal User user, @PathVariable long deckId) {
    var deck = flashcardService.getDeck(user, deckId);
    flashcardService.deleteDeck(user, deckId);
    return ResponseEntity.status(204).build();
  }

  private String deriveFilename(Deck deck, ExportDataType exportDataType) {
    return deck.getName() + exportDataType.getExtension();
  }
}
