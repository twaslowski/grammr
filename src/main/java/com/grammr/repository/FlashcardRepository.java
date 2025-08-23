package com.grammr.repository;

import com.grammr.domain.entity.Deck;
import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.entity.Flashcard.Status;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

  List<Flashcard> findByDeckId(long id);

  Optional<Flashcard> findByFlashcardIdAndDeck(UUID id, Deck deck);

  List<Flashcard> findByDeckAndStatusIn(Deck deck, Set<Status> statuses);

  Optional<Flashcard> findByFlashcardId(UUID flashcardId);

  Optional<Flashcard> findByFrontAndDeck(String front, Deck deck);

  List<Flashcard> findAllByFlashcardIdInAndDeckEquals(Collection<UUID> flashcardIds, Deck deck);
}
