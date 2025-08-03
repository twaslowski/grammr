package com.grammr.service;

import com.grammr.domain.entity.DeckSpec;
import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.entity.FlashcardSpec;
import com.grammr.domain.entity.Paradigm;
import com.grammr.domain.entity.UserSpec;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.exception.ResourceExistsException;
import com.grammr.flashcards.service.DeckService;
import com.grammr.flashcards.service.FlashcardService;
import com.grammr.repository.FlashcardRepository;
import com.grammr.repository.ParadigmRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlashcardServiceTest {

  @InjectMocks
  private FlashcardService flashcardService;

  @Mock
  private DeckService deckService;

  @Mock
  private ParadigmRepository paradigmRepository;

  @Mock
  private FlashcardRepository flashcardRepository;

  @Test
  void shouldCreateFlashcardSuccessfully() {
    var user = UserSpec.valid().build();
    var deck = DeckSpec.withUser(user).build();
    var paradigmId = UUID.randomUUID();
    var question = "What is the capital of France?";
    var answer = "Paris";
    var tokenPos = PartOfSpeechTag.NOUN;

    when(deckService.getDeck(deck.getDeckId(), user)).thenReturn(deck);
    when(paradigmRepository.findById(paradigmId)).thenReturn(Optional.empty());
    when(flashcardRepository.save(any(Flashcard.class))).thenAnswer(invocation -> invocation.getArgument(0));

    var flashcard = flashcardService.createFlashcard(user, deck.getDeckId(), question, answer, tokenPos, paradigmId);

    assertThat(flashcard).isNotNull();
    assertThat(flashcard.getFront()).isEqualTo(question);
    assertThat(flashcard.getBack()).isEqualTo(answer);
    assertThat(flashcard.getTokenPos()).isEqualTo(tokenPos);
    assertThat(flashcard.getDeck()).isEqualTo(deck);
    assertThat(flashcard.getParadigm()).isNull();
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

    when(deckService.getDeck(deck.getDeckId(), user)).thenReturn(deck);
    when(paradigmRepository.findById(paradigmId)).thenReturn(Optional.of(paradigm));
    when(flashcardRepository.save(any(Flashcard.class))).thenAnswer(invocation -> invocation.getArgument(0));

    var flashcard = flashcardService.createFlashcard(user, deck.getDeckId(), question, answer, tokenPos, paradigmId);

    assertThat(flashcard).isNotNull();
    assertThat(flashcard.getFront()).isEqualTo(question);
    assertThat(flashcard.getBack()).isEqualTo(answer);
    assertThat(flashcard.getTokenPos()).isEqualTo(tokenPos);
    assertThat(flashcard.getDeck()).isEqualTo(deck);
    assertThat(flashcard.getParadigm()).isEqualTo(paradigm);
  }

  @ParameterizedTest
  @EnumSource(value = Flashcard.Status.class, names = {"CREATED", "UPDATED", "MARKED_FOR_DELETION"})
  void shouldRetrieveSyncableFlashcards(Flashcard.Status status) {
    var deck = DeckSpec.withUser(UserSpec.valid().build()).build();
    var flashcard = FlashcardSpec.withDeck(deck).status(status).build();

    when(flashcardRepository.findByDeckAndStatusIn(
        deck,
        Set.of(Flashcard.Status.CREATED, Flashcard.Status.UPDATED, Flashcard.Status.MARKED_FOR_DELETION))
    ).thenReturn(List.of(flashcard));

    var syncableCards = flashcardService.retrieveSyncableCards(deck);

    assertThat(syncableCards).hasSize(1);
    assertThat(syncableCards.getFirst().status()).isEqualTo(status);
  }

  @Test
  void shouldThrowExceptionIfFlashcardWithIdenticalQuestionExists() {
    var user = UserSpec.valid().build();
    var deck = DeckSpec.withUser(user).build();
    var question = "What is the capital of France?";
    var answer = "Paris";

    when(deckService.getDeck(deck.getDeckId(), user)).thenReturn(deck);
    when(flashcardRepository.findByFrontAndDeck(question, deck)).thenReturn(Optional.of(new Flashcard()));

    assertThatThrownBy(() -> flashcardService.createFlashcard(
        user, deck.getDeckId(), question, answer, null, null)
    ).isInstanceOf(ResourceExistsException.class);
  }
}