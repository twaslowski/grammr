package com.grammr.flashcards.service;

import com.grammr.domain.entity.Deck;
import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.entity.Flashcard.Status;
import com.grammr.domain.entity.Flashcard.Type;
import com.grammr.domain.entity.User;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.exception.ResourceExistsException;
import com.grammr.domain.exception.ResourceNotFoundException;
import com.grammr.flashcards.controller.v2.dto.FlashcardCreationDto;
import com.grammr.flashcards.controller.v2.dto.FlashcardDto;
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
@Transactional
@RequiredArgsConstructor
public class FlashcardService {

  private final FlashcardRepository flashcardRepository;
  private final DeckService deckService;
  private final ParadigmRepository paradigmRepository;

  public List<Flashcard> getFlashcards(Deck deck) {
    log.info("Retrieving flashcards for deck {}", deck.getDeckId());
    return flashcardRepository.findByDeckId(deck.getId());
  }

  public List<FlashcardDto> retrieveSyncableCards(Deck deck) {
    var flashcards = flashcardRepository.findByDeckAndStatusIn(
        deck,
        Set.of(Status.CREATED, Status.UPDATED, Status.MARKED_FOR_DELETION)
    );

    var dtos = flashcards.stream()
        .map(FlashcardDto::fromEntity)
        .toList();

    log.info("Initiated flashcard sync for deck {}", deck.getDeckId());
    return dtos;
  }

  public void confirmSync(Deck deck, List<UUID> successfulSyncIds, List<UUID> failedSyncIds) {
    List<Flashcard> successfulSyncs = flashcardRepository.findAllByFlashcardIdInAndDeckEquals(successfulSyncIds, deck);
    List<Flashcard> failedSyncs = flashcardRepository.findAllByFlashcardIdInAndDeckEquals(failedSyncIds, deck);

    log.info("Confirming successful sync for {} flashcards", successfulSyncs.size());
    successfulSyncs.forEach(flashcard -> {
      if (flashcard.getStatus() == Status.MARKED_FOR_DELETION) {
        log.info("Deleting flashcard after successful sync: {}", flashcard.getFlashcardId());
        flashcardRepository.delete(flashcard);
      }
      flashcard.confirmSync();
    });

    if (!failedSyncs.isEmpty()) {
      log.warn("{} flashcards failed to sync", failedSyncs.size());
    }
  }

  public Flashcard getFlashcard(UUID flashcardId, Deck deck) {
    log.info("Retrieving flashcard {} for deck {}", flashcardId, deck.getDeckId());
    return flashcardRepository.findByFlashcardIdAndDeck(flashcardId, deck)
        .orElseThrow(() -> new ResourceNotFoundException("Flashcard not found: %s in deck %s".formatted(flashcardId, deck.getDeckId())));
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

    Type type = paradigm == null || tokenPos == null ? Type.BASIC : Type.INFLECTION;

    var flashcard = Flashcard.builder()
        .front(question)
        .back(answer)
        .flashcardId(UUID.randomUUID())
        .tokenPos(tokenPos)
        .paradigm(paradigm)
        .type(type)
        .status(Status.CREATED)
        .deck(deck)
        .build();

    return flashcardRepository.save(flashcard);
  }

  public void deleteFlashcard(UUID flashcardId) {
    var foundFlashcard = flashcardRepository.findByFlashcardId(flashcardId)
        .orElseThrow(() -> new ResourceNotFoundException(String.valueOf(flashcardId)));

    foundFlashcard.setStatus(Status.MARKED_FOR_DELETION);
    flashcardRepository.save(foundFlashcard);
  }

  public void resetDeckSync(Deck deck) {
    var flashcards = flashcardRepository.findByDeckId(deck.getId());
    flashcards.forEach(flashcard -> flashcard.setStatus(Status.CREATED));
    flashcardRepository.saveAll(flashcards);
    log.info("Reset sync status for all flashcards in deck {}", deck.getDeckId());
  }
}
