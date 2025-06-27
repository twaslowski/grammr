package com.grammr.repository;

import com.grammr.domain.entity.Deck;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeckRepository extends JpaRepository<Deck, Long> {

  List<Deck> findAllByOwnerId(UUID ownerId);

  @Query("SELECT d FROM Deck d WHERE d.deckId = :deckId AND d.owner.id = :ownerId")
  Optional<Deck> findByDeckIdAndOwnerId(@Param("deckId") UUID deckId, @Param("ownerId") UUID ownerId);
}
