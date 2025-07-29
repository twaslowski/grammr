package com.grammr.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.grammr.domain.entity.DeckSpec;
import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.entity.Paradigm;
import com.grammr.domain.entity.UserSpec;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.flashcards.service.DeckService;
import com.grammr.flashcards.service.FlashcardService;
import com.grammr.repository.FlashcardRepository;
import com.grammr.repository.ParadigmRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    assertThat(flashcard.getQuestion()).isEqualTo(question);
    assertThat(flashcard.getAnswer()).isEqualTo(answer);
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
    assertThat(flashcard.getQuestion()).isEqualTo(question);
    assertThat(flashcard.getAnswer()).isEqualTo(answer);
    assertThat(flashcard.getTokenPos()).isEqualTo(tokenPos);
    assertThat(flashcard.getDeck()).isEqualTo(deck);
    assertThat(flashcard.getParadigm()).isEqualTo(paradigm);
  }

  @Test
  void shouldRetrieveSyncableFlashcards() {
    var deckId = 1L;
    var flashcard1 = Flashcard.builder().id(1L).status(Flashcard.Status.CREATED).build();
    var flashcard2 = Flashcard.builder().id(2L).status(Flashcard.Status.UPDATED).build();

    when(flashcardRepository.findByDeckIdAndStatusIn(deckId, Set.of(Flashcard.Status.CREATED, Flashcard.Status.UPDATED)))
        .thenReturn(List.of(flashcard1, flashcard2));

    var syncId = UUID.randomUUID();
    var syncableCards = flashcardService.retrieveSyncableCards(deckId, syncId);

    assertThat(syncableCards).hasSize(2);
    assertThat(syncableCards.get(0).getStatus()).isEqualTo(Flashcard.Status.EXPORT_INITIATED);
    assertThat(syncableCards.get(1).getStatus()).isEqualTo(Flashcard.Status.EXPORT_INITIATED);
  }
}