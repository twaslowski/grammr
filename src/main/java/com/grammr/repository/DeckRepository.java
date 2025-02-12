package com.grammr.repository;

import com.grammr.domain.entity.Deck;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRepository extends JpaRepository<Deck, Long> {

  Optional<Deck> findByUserId(long userId);
}
