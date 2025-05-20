package com.grammr.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.grammr.controller.dto.FlashcardCreationDto;
import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.entity.User;
import com.grammr.service.AnkiService;
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
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/flashcard")
public class FlashcardController {

  private final AnkiService ankiService;

  @PostMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Flashcard> createFlashcard(@RequestBody @Valid FlashcardCreationDto data,
                                                   @AuthenticationPrincipal User user) {
    log.info("Creating flashcard for user {} in deck {}", user.getId(), data.deckId());
    var flashcard = ankiService.createFlashcard(user, data.deckId(), data.question(), data.answer(), data.tokenPos(), data.paradigmId());
    return ResponseEntity.status(201).body(flashcard);
  }

  @DeleteMapping("/{flashcardId}")
  public ResponseEntity<?> deleteFlashcard(@AuthenticationPrincipal User user, @PathVariable long flashcardId) {
    ankiService.deleteFlashcard(user, flashcardId);
    return ResponseEntity.status(204).build();
  }
}
