package com.grammr.flashcards.controller.v1;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.entity.User;
import com.grammr.flashcards.controller.dto.FlashcardCreationDto;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Flashcards", description = "Flashcard management operations")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/flashcard")
public class FlashcardController {

  private final FlashcardService flashcardService;

  @Operation(summary = "Create a new flashcard", description = "Creates a new flashcard in the specified deck.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Flashcard created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input"),
      @ApiResponse(responseCode = "404", description = "Deck not found")
  })
  @PostMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Flashcard> createFlashcard(
      @Parameter(description = "Flashcard creation data") @RequestBody @Valid FlashcardCreationDto data,
      @Parameter(description = "Authenticated user") @AuthenticationPrincipal User user) {
    log.info("Creating flashcard for user {} in deck {}", user.getId(), data.deckId());
    var flashcard = flashcardService.createFlashcard(user, data.deckId(), data.question(), data.answer(), data.tokenPos(), data.paradigmId());
    return ResponseEntity.status(201).body(flashcard);
  }

  @Operation(summary = "Delete a flashcard", description = "Deletes a flashcard by its ID")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Flashcard deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Flashcard not found")
  })
  @DeleteMapping("/{flashcardId}")
  public ResponseEntity<?> deleteFlashcard(
      @Parameter(description = "Authenticated user") @AuthenticationPrincipal User user,
      @Parameter(description = "ID of the flashcard to delete") @PathVariable long flashcardId) {
    flashcardService.deleteFlashcard(user, flashcardId);
    return ResponseEntity.status(204).build();
  }
}
