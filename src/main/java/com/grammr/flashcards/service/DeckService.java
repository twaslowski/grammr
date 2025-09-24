package com.grammr.flashcards.service;

import com.grammr.domain.entity.Deck;
import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.entity.User;
import com.grammr.domain.enums.ExportDataType;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.exception.DeckNotFoundException;
import com.grammr.flashcards.controller.v2.dto.DeckDumpDto;
import com.grammr.flashcards.port.AnkiPort;
import com.grammr.repository.DeckRepository;
import com.grammr.repository.FlashcardRepository;
import com.grammr.repository.ParadigmRepository;
import java.util.List;
import java.util.Optional;
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
  private final ParadigmRepository paradigmRepository;

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

  public Deck updateDeck(Deck deck, String name, String description) {
    if (name != null) {
      deck.setName(name);
    }

    if (description != null) {
      deck.setDescription(description);
    }

    return deckRepository.save(deck);
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

  /**
   * Create a JSON-serializable dump of a deck and its flashcards.
   */
  public DeckDumpDto dumpDeck(Deck deck) {
    var flashcards = flashcardRepository.findByDeckId(deck.getId());
    var cardDtos = flashcards.stream()
        .map(card -> new DeckDumpDto.FlashcardDumpDto(
            card.getFlashcardId(),
            card.getFront(),
            card.getBack(),
            card.getTokenPos() != null ? card.getTokenPos().name() : null,
            card.getParadigmId() != null ? UUID.fromString(card.getParadigmId()) : null
        ))
        .toList();

    return new DeckDumpDto(deck.getDeckId(), deck.getName(), deck.getDescription(), cardDtos);
  }

  /**
   * Import a deck dump for the given user. Creates a new deck and flashcards.
   * New internal UUIDs are generated for deckId and flashcardIds to avoid collisions.
   */
  public Deck importDeck(User user, DeckDumpDto dump) {
    var newDeck = Deck.builder()
        .name(dump.name())
        .description(dump.description())
        .deckId(UUID.randomUUID())
        .owner(user)
        .build();

    var savedDeck = deckRepository.save(newDeck);

    dump.flashcards().forEach(card -> {
      var paradigm = Optional.ofNullable(card.paradigmId())
          .flatMap(paradigmRepository::findById)
          .orElse(null);

      PartOfSpeechTag tokenPos = null;
      if (card.tokenPos() != null) {
        try {
          tokenPos = PartOfSpeechTag.valueOf(card.tokenPos());
        } catch (IllegalArgumentException e) {
          // ignore unknown token pos
        }
      }

      var flashcard = Flashcard.builder()
          .front(card.front())
          .back(card.back())
          .flashcardId(UUID.randomUUID())
          .tokenPos(tokenPos)
          .paradigm(paradigm)
          .status(Flashcard.Status.CREATED)
          .deck(savedDeck)
          .build();

      flashcardRepository.save(flashcard);
    });

    return savedDeck;
  }
}
