package com.grammr.flashcards.service;

import com.grammr.domain.entity.Deck;
import com.grammr.domain.entity.User;
import com.grammr.domain.enums.ExportDataType;
import com.grammr.domain.exception.DeckNotFoundException;
import com.grammr.flashcards.port.AnkiPort;
import com.grammr.repository.DeckRepository;
import com.grammr.repository.FlashcardRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeckService {

  private final FlashcardRepository flashcardRepository;
  private final DeckRepository deckRepository;
  private final AnkiPort ankiPort;

  public Deck createDeck(User user, String name, String description) {
    var deck = Deck.builder()
        .name(name)
        .description(description)
        .deckId(UUID.randomUUID())
        .owner(user)
        .build();
    return deckRepository.save(deck);
  }

  public Deck getDeck(UUID deckId, User user) {
    return deckRepository.findByDeckIdAndOwnerId(deckId, user.getId())
        .orElseThrow(() -> new DeckNotFoundException(deckId));
  }

  public void deleteDeck(UUID deckId, User user) {
    var foundDeck = getDeck(deckId, user);
    deckRepository.delete(foundDeck);
  }

  public byte[] exportDeck(Deck deck, ExportDataType exportDataType) {
    var flashcards = flashcardRepository.findByDeckId(deck.getId());
    return switch (exportDataType) {
      case APKG, DB -> ankiPort.exportDeck(deck, flashcards);
    };
  }

  public List<Deck> getDecks(User user) {
    return deckRepository.findAllByOwnerId(user.getId());
  }
}
