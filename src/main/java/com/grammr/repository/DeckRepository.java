package com.grammr.repository;

import com.grammr.domain.entity.Deck;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRepository extends JpaRepository<Deck, Long> {

  List<Deck> findAllByUserId(long userId);
}
