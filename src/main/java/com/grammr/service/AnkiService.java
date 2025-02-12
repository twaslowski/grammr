package com.grammr.service;

import com.grammr.domain.entity.Deck;
import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.exception.DeckNotFoundException;
import com.grammr.domain.exception.UserNotFoundException;
import com.grammr.port.outbound.AnkiPort;
import com.grammr.repository.DeckRepository;
import com.grammr.repository.FlashcardRepository;
import com.grammr.repository.UserRepository;
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
  private final UserRepository userRepository;

  @Transactional
  public byte[] exportDeck(long id) {
    var deck = deckRepository.findById(id)
        .orElseThrow(() -> new DeckNotFoundException(id));
    var flashcards = flashcardRepository.findByDeckId(deck.getId());
    return ankiPort.exportDeck(deck, flashcards);
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
}
