package com.grammr.service;

import com.grammr.controller.dto.DeckDto;
import com.grammr.domain.entity.Deck;
import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.entity.Flashcard.Status;
import com.grammr.domain.entity.User;
import com.grammr.domain.enums.ExportDataType;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.exception.DeckNotFoundException;
import com.grammr.domain.exception.UserNotFoundException;
import com.grammr.port.AnkiPort;
import com.grammr.repository.DeckRepository;
import com.grammr.repository.FlashcardRepository;
import com.grammr.repository.ParadigmRepository;
import com.grammr.repository.UserRepository;
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
public class AnkiService {

  private final FlashcardRepository flashcardRepository;
  private final DeckRepository deckRepository;
  private final AnkiPort ankiPort;
  private final UserRepository userRepository;
  private final ParadigmRepository paradigmRepository;

  public byte[] exportDeck(Deck deck, ExportDataType exportDataType) {
    var flashcards = flashcardRepository.findByDeckId(deck.getId());
    return switch (exportDataType) {
      case APKG, DB -> ankiPort.exportDeck(deck, flashcards);
    };
  }

  public List<Flashcard> retrieveSyncableCards(long deckId) {
    var flashcards = flashcardRepository.findByDeckIdAndStatusNot(deckId, Status.EXPORTED);
    flashcards.forEach(f -> f.setStatus(Status.EXPORTED));
    log.info("Synced {} cards for deck {}", flashcards.size(), deckId);
    return flashcards;
  }

  public Flashcard createFlashcard(User user, long deckId, String question, String answer, PartOfSpeechTag tokenPos, UUID paradigmId) {
    var deck = deckRepository.findById(deckId)
        .map(d -> checkOwnershipMismatch(user, d))
        .orElseThrow(() -> new DeckNotFoundException(user.getId(), deckId));
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

  public Deck createDeck(String userId, String name) {
    var user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
    var deck = Deck.builder()
        .name(name)
        .user(user)
        .build();
    return deckRepository.save(deck);
  }

  public Deck getDeck(User user, long deckId) {
    return deckRepository.findById(deckId)
        .map(d -> checkOwnershipMismatch(user, d))
        .orElseThrow(() -> new DeckNotFoundException(user.getId(), deckId));
  }

  public List<DeckDto> getDecks(String userId) {
    var decks = deckRepository.findAllByUserId(userId);
    return decks.stream()
        .map(deck -> new DeckDto(deck, flashcardRepository.findByDeckId(deck.getId())))
        .toList();
  }

  public List<Flashcard> getFlashcards(long deckId) {
    return flashcardRepository.findByDeckId(deckId);
  }

  public void deleteDeck(User user, long deckId) {
    var foundDeck = getDeck(user, deckId);
    deckRepository.delete(foundDeck);
  }

  public void deleteFlashcard(User user, long flashcardId) {
    var foundFlashcard = flashcardRepository.findById(flashcardId)
        .filter(flashcard -> Objects.equals(flashcard.getDeck().getUser().getId(), user.getId()))
        .orElseThrow(() -> new DeckNotFoundException(user.getId(), flashcardId));
    flashcardRepository.delete(foundFlashcard);
  }

  private Deck checkOwnershipMismatch(User user, Deck deck) {
    if (!deck.getUser().getId().equals(user.getId())) {
      throw new DeckNotFoundException(user.getId(), deck.getId());
    }
    return deck;
  }
}
