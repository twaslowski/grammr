package com.grammr.flashcards.service;

import com.grammr.domain.entity.Deck;
import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.entity.Flashcard.Status;
import com.grammr.domain.entity.User;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.exception.ResourceNotFoundException;
import com.grammr.repository.DeckRepository;
import com.grammr.repository.FlashcardRepository;
import com.grammr.repository.ParadigmRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

  public List<Flashcard> retrieveSyncableCards(long deckId) {
    var flashcards = flashcardRepository.findByDeckIdAndStatusNot(deckId, Status.EXPORTED);
    flashcards.forEach(f -> f.setStatus(Status.EXPORTED));
    log.info("Synced {} cards for deck {}", flashcards.size(), deckId);
    return flashcards;
  }

  public Flashcard createFlashcard(User user, UUID deckId, String question, String answer, PartOfSpeechTag tokenPos, UUID paradigmId) {
    var deck = deckService.getDeck(deckId, user);
    var paradigm = Optional.ofNullable(paradigmId)
        .flatMap(paradigmRepository::findById)
        .orElse(null);
    var flashcard = Flashcard.builder()
        .question(question)
        .answer(answer)
        .tokenPos(tokenPos)
        .paradigm(paradigm)
        .status(Status.CREATED)
        .deck(deck).build();
    return flashcardRepository.save(flashcard);
  }

  public void deleteFlashcard(User user, long flashcardId) {
    var foundFlashcard = flashcardRepository.findById(flashcardId)
        .filter(flashcard -> Objects.equals(flashcard.getDeck().getOwner().getId(), user.getId()))
        .orElseThrow(() -> new ResourceNotFoundException(String.valueOf(flashcardId)));
    flashcardRepository.delete(foundFlashcard);
  }
}
