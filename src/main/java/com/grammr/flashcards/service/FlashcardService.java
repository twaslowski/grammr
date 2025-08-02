package com.grammr.flashcards.service;

import com.grammr.domain.entity.Deck;
import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.entity.Flashcard.Status;
import com.grammr.domain.entity.User;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.exception.ResourceExistsException;
import com.grammr.domain.exception.ResourceNotFoundException;
import com.grammr.flashcards.controller.v2.dto.FlashcardCreationDto;
import com.grammr.repository.FlashcardRepository;
import com.grammr.repository.ParadigmRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FlashcardService {

  private final FlashcardRepository flashcardRepository;
  private final DeckService deckService;
  private final ParadigmRepository paradigmRepository;

  public List<Flashcard> getFlashcards(Deck deck) {
    log.info("Retrieving flashcards for deck {}", deck.getDeckId());
    return flashcardRepository.findByDeckId(deck.getId());
  }

  public List<Flashcard> retrieveSyncableCards(long deckId, UUID syncId) {
    var flashcards = flashcardRepository.findByDeckIdAndStatusIn(deckId, Set.of(Status.CREATED, Status.UPDATED));
    flashcards.forEach(f -> f.initiateSync(syncId));
    log.info("Initiated flashcard sync for deck {}", deckId);
    return flashcards;
  }

  public void confirmSync(long id, UUID syncId) {
    List<Flashcard> flashcards = flashcardRepository.findByDeckIdAndSyncId(id, syncId);

    if (flashcards.isEmpty()) {
      log.warn("No flashcards found for deck {} with syncId {}", id, syncId);
      throw new ResourceNotFoundException(syncId.toString());
    }

    log.info("Confirming sync for {} flashcards in deck {}", flashcards.size(), id);
    flashcards.forEach(Flashcard::confirmSync);
  }

  public Flashcard getFlashcard(UUID flashcardId, long deckId) {
    log.info("Retrieving flashcard {} for deck {}", flashcardId, deckId);
    return flashcardRepository.findByFlashcardIdAndDeckId(flashcardId, deckId)
        .orElseThrow(() -> new ResourceNotFoundException("Flashcard not found: %s in deck %s".formatted(flashcardId, deckId)));
  }

  public Flashcard updateFlashcardWith(Flashcard flashcard, FlashcardCreationDto data) {
    log.info("Updating flashcard {}", flashcard.getFlashcardId());
    return flashcardRepository.save(flashcard.updateWith(data));
  }

  public Flashcard createFlashcard(User user, UUID deckId, String question, String answer, PartOfSpeechTag tokenPos, UUID paradigmId) {
    var deck = deckService.getDeck(deckId, user);

    var existingFlashcard = flashcardRepository.findByFrontAndDeck(question, deck);
    if (existingFlashcard.isPresent()) {
      throw new ResourceExistsException("Flashcard with the same question already exists: %s".formatted(question));
    }

    var paradigm = Optional.ofNullable(paradigmId)
        .flatMap(paradigmRepository::findById)
        .orElse(null);

    var flashcard = Flashcard.builder()
        .front(question)
        .back(answer)
        .flashcardId(UUID.randomUUID())
        .tokenPos(tokenPos)
        .paradigm(paradigm)
        .status(Status.CREATED)
        .deck(deck).build();

    return flashcardRepository.save(flashcard);
  }

  public void deleteFlashcard(UUID flashcardId) {
    var foundFlashcard = flashcardRepository.findByFlashcardId(flashcardId)
        .orElseThrow(() -> new ResourceNotFoundException(String.valueOf(flashcardId)));

    flashcardRepository.delete(foundFlashcard);
  }
}
