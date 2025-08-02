package com.grammr.flashcards.controller.v2;

import com.grammr.domain.entity.User;
import com.grammr.flashcards.controller.v2.dto.FlashcardCreationDto;
import com.grammr.flashcards.controller.v2.dto.FlashcardDto;
import com.grammr.flashcards.service.DeckService;
import com.grammr.flashcards.service.FlashcardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
@Tag(name = "Flashcards", description = "Flashcard management operations")
@RequiredArgsConstructor
@RestController(value = "flashcardControllerV2")
@RequestMapping("/api/v2/deck/{deckId}/flashcard")
public class FlashcardController {

  private final FlashcardService flashcardService;
  private final DeckService deckService;

  @GetMapping
  @Operation(summary = "Get flashcards in a deck", description = "Retrieves all flashcards in the specified deck.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Flashcards retrieved successfully"),
      @ApiResponse(responseCode = "404", description = "Deck not found")
  })
  public ResponseEntity<List<FlashcardDto>> getFlashcards(
      @PathVariable UUID deckId,
      @Parameter(description = "Authenticated user") @AuthenticationPrincipal User user) {
    log.info("Retrieving flashcards for user {} in deck {}", user.getId(), deckId);
    var deck = deckService.getDeck(deckId, user);
    var flashcards = flashcardService.getFlashcards(deck);
    return ResponseEntity.ok(flashcards.stream().map(FlashcardDto::fromEntity).toList());
  }

  @Operation(summary = "Create a new flashcard", description = "Creates a new flashcard in the specified deck.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Flashcard created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input"),
      @ApiResponse(responseCode = "404", description = "Deck not found")
  })
  @PostMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<FlashcardDto> createFlashcard(
      @PathVariable UUID deckId,
      @Parameter(description = "Flashcard creation data") @RequestBody @Valid FlashcardCreationDto data,
      @Parameter(description = "Authenticated user") @AuthenticationPrincipal User user) {
    log.info("Creating flashcard for user {} in deck {}", user.getId(), deckId);
    var deck = deckService.getDeck(deckId, user);
    var flashcard = flashcardService.createFlashcard(
        user, deck.getDeckId(), data.question(), data.answer(), data.tokenPos(), data.paradigmId()
    );
    return ResponseEntity.status(201).body(FlashcardDto.fromEntity(flashcard));
  }

  @Operation(summary = "Modify an existing flashcard", description = "Creates a new flashcard in the specified deck.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Flashcard created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input"),
      @ApiResponse(responseCode = "404", description = "Deck not found")
  })
  @PutMapping(path = "/{flashcardId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<FlashcardDto> updateFlashcard(
      @PathVariable UUID deckId,
      @PathVariable UUID flashcardId,
      @Parameter(description = "Flashcard creation data") @RequestBody @Valid FlashcardCreationDto data,
      @Parameter(description = "Authenticated user") @AuthenticationPrincipal User user) {
    log.info("Attempting to update flashcard for user {} in deck {}", user.getId(), deckId);
    // This approach incurs a performance overhead, but I don't have to perform the extra flashcard ownership check
    // If this turns out to be a performance bottleneck, we can optimize it later
    var deck = deckService.getDeck(deckId, user);
    var flashcard = flashcardService.getFlashcard(flashcardId, deck);
    flashcard = flashcardService.updateFlashcardWith(flashcard, data);
    return ResponseEntity.ok().body(FlashcardDto.fromEntity(flashcard));
  }


  @Operation(summary = "Delete a flashcard", description = "Deletes a flashcard by its ID")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Flashcard deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Flashcard not found")
  })
  @DeleteMapping("/{flashcardId}")
  public ResponseEntity<Void> deleteFlashcard(
      @PathVariable UUID deckId,
      @Parameter(description = "Authenticated user") @AuthenticationPrincipal User user,
      @Parameter(description = "ID of the flashcard to delete") @PathVariable UUID flashcardId) {
    // getDeck is called for its DeckNotFoundException side effect to ensure resource ownership
    deckService.getDeck(deckId, user);

    flashcardService.deleteFlashcard(flashcardId);
    return ResponseEntity.status(204).build();
  }
}
