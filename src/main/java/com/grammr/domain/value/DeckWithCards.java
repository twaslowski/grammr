package com.grammr.domain.value;

import com.grammr.domain.entity.Deck;
import com.grammr.domain.entity.Flashcard;
import lombok.Builder;
import java.util.List;

@Builder
public record DeckWithCards(
    Deck deck,
    List<Flashcard> flashcards
) {

}
