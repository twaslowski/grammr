package com.grammr.repository;

import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.entity.Flashcard.Status;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

  List<Flashcard> findByDeckId(long id);

  List<Flashcard> findByDeckIdAndStatusIn(long deckId, Set<Status> statuses);

  List<Flashcard> findByDeckIdAndSyncId(long deckId, UUID syncId);

  Optional<Flashcard> findByFlashcardId(UUID flashcardId);
}
