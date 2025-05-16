package com.grammr.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.grammr.domain.entity.DeckSpec;
import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.entity.Paradigm;
import com.grammr.domain.entity.UserSpec;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.exception.DeckNotFoundException;
import com.grammr.repository.DeckRepository;
import java.util.Optional;
import java.util.UUID;
import com.grammr.repository.FlashcardRepository;
import com.grammr.repository.ParadigmRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnkiServiceTest {

  @Mock
  private DeckRepository deckRepository;

  @InjectMocks
  private AnkiService ankiService;

  @Mock
  private ParadigmRepository paradigmRepository;

  @Mock
  private FlashcardRepository flashcardRepository;

  @Test
  void shouldThrowExceptionOnOwnerMismatch() {
    var user = UserSpec.valid().build();
    var otherUser = UserSpec.valid().build();
    var deck = DeckSpec.withUser(user).build();

    when(deckRepository.findById(1L)).thenReturn(Optional.of(deck));

    assertThatThrownBy(() -> ankiService.deleteDeck(otherUser, deck.getId()))
        .isInstanceOf(DeckNotFoundException.class);
    assertThatThrownBy(() -> ankiService.getDeck(otherUser, deck.getId()))
        .isInstanceOf(DeckNotFoundException.class);
  }

  @Test
  void shouldReturnDeckWithMatchingOwner() {
    var user = UserSpec.valid().build();
    var deck = DeckSpec.withUser(user).build();

    when(deckRepository.findById(1L)).thenReturn(Optional.of(deck));

    assertThat(ankiService.getDeck(user, deck.getId())).isEqualTo(deck);
  }

  @Test
  void shouldCreateFlashcardSuccessfully() {
    var user = UserSpec.valid().build();
    var deck = DeckSpec.withUser(user).build();
    var paradigmId = UUID.randomUUID();
    var question = "What is the capital of France?";
    var answer = "Paris";
    var tokenPos = PartOfSpeechTag.NOUN;

    when(deckRepository.findById(deck.getId())).thenReturn(Optional.of(deck));
    when(paradigmRepository.findById(paradigmId)).thenReturn(Optional.empty());
    when(flashcardRepository.save(any(Flashcard.class))).thenAnswer(invocation -> invocation.getArgument(0));

    var flashcard = ankiService.createFlashcard(user, deck.getId(), question, answer, tokenPos, paradigmId);

    assertThat(flashcard).isNotNull();
    assertThat(flashcard.getQuestion()).isEqualTo(question);
    assertThat(flashcard.getAnswer()).isEqualTo(answer);
    assertThat(flashcard.getTokenPos()).isEqualTo(tokenPos);
    assertThat(flashcard.getDeck()).isEqualTo(deck);
    assertThat(flashcard.getParadim()).isNull();
  }

  @Test
  void shouldCreateFlashcardWithReferenceToParadigm() {
    var user = UserSpec.valid().build();
    var deck = DeckSpec.withUser(user).build();
    var paradigmId = UUID.randomUUID();
    var question = "What is the capital of France?";
    var answer = "Paris";
    var tokenPos = PartOfSpeechTag.NOUN;
    Paradigm paradigm = Paradigm.builder().id(paradigmId).build();

    when(deckRepository.findById(deck.getId())).thenReturn(Optional.of(deck));
    when(paradigmRepository.findById(paradigmId)).thenReturn(Optional.of(paradigm));
    when(flashcardRepository.save(any(Flashcard.class))).thenAnswer(invocation -> invocation.getArgument(0));

    var flashcard = ankiService.createFlashcard(user, deck.getId(), question, answer, tokenPos, paradigmId);

    assertThat(flashcard).isNotNull();
    assertThat(flashcard.getQuestion()).isEqualTo(question);
    assertThat(flashcard.getAnswer()).isEqualTo(answer);
    assertThat(flashcard.getTokenPos()).isEqualTo(tokenPos);
    assertThat(flashcard.getDeck()).isEqualTo(deck);
    assertThat(flashcard.getParadim()).isEqualTo(paradigm);
  }


  @Test
  void shouldThrowExceptionWhenDeckNotFound() {
    var user = UserSpec.valid().build();
    var deckId = 1L;

    when(deckRepository.findById(deckId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> ankiService.createFlashcard(user, deckId, "Q", "A", PartOfSpeechTag.NOUN, UUID.randomUUID()))
        .isInstanceOf(DeckNotFoundException.class)
        .hasMessageContaining(String.valueOf(deckId));
  }
}