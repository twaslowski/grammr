package com.grammr.repository;

import com.grammr.domain.entity.Flashcard;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

  List<Flashcard> findByDeckId(long id);
}
