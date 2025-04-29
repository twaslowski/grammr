package com.grammr.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.grammr.domain.entity.DeckSpec;
import com.grammr.domain.entity.UserSpec;
import com.grammr.domain.exception.DeckNotFoundException;
import com.grammr.repository.DeckRepository;
import java.util.Optional;
import java.util.UUID;
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
}