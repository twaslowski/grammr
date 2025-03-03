package com.grammr.service;

import com.grammr.domain.entity.Deck;
import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.enums.ExportDataType;
import com.grammr.domain.exception.DeckNotFoundException;
import com.grammr.domain.exception.UserNotFoundException;
import com.grammr.port.dto.DeckDTO;
import com.grammr.port.outbound.AnkiPort;
import com.grammr.repository.DeckRepository;
import com.grammr.repository.FlashcardRepository;
import com.grammr.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnkiService {

  private final FlashcardRepository flashcardRepository;
  private final DeckRepository deckRepository;
  private final AnkiPort ankiPort;
  private final AnkiCsvExportService ankiCsvExportService;
  private final UserRepository userRepository;

  @Transactional
  public byte[] exportDeck(long id, ExportDataType exportDataType) {
    var deck = deckRepository.findById(id)
        .orElseThrow(() -> new DeckNotFoundException(id));
    var flashcards = flashcardRepository.findByDeckId(deck.getId());
    if (exportDataType == null) {
      exportDataType = ExportDataType.CSV;
    }
    return switch (exportDataType) {
      case CSV -> ankiCsvExportService.exportDeck(flashcards);
      case APKG, DB -> ankiPort.exportDeck(deck, flashcards);
    };
  }

  @Transactional
  public Flashcard createFlashcard(long deckId, String question, String answer) {
    var deck = deckRepository.findById(deckId)
        .orElseThrow(() -> new DeckNotFoundException(deckId));
    var flashcard = Flashcard.builder()
        .question(question)
        .answer(answer)
        .deck(deck).build();
    return flashcardRepository.save(flashcard);
  }

  @Transactional
  public Deck createDeck(long userId, String name) {
    var user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
    var deck = Deck.builder()
        .name(name)
        .user(user)
        .build();
    return deckRepository.save(deck);
  }

  public Deck getDeck(long userId, long deckId) {
    return deckRepository.findById(deckId)
        .filter(deck -> deck.getUser().getId() == userId)
        .orElseThrow(() -> new DeckNotFoundException(deckId));
  }

  public List<DeckDTO> getDecks(long userId) {
    var decks = deckRepository.findAllByUserId(userId);
    return decks.stream()
        .map(deck -> new DeckDTO(deck, flashcardRepository.findByDeckId(deck.getId())))
        .toList();
  }

  public List<Flashcard> getFlashcards(long deckId) {
    return flashcardRepository.findByDeckId(deckId);
  }

  public void deleteDeck(long userId, long deckId) {
    deckRepository.findById(deckId)
        .filter(d -> d.getUser().getId() == userId)
        .orElseThrow(() -> new DeckNotFoundException(deckId));
    deckRepository.deleteById(deckId);
  }
}
