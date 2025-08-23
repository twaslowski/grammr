package com.grammr.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.entity.DeckSpec;
import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.entity.FlashcardSpec;
import com.grammr.domain.entity.UserSpec;
import com.grammr.flashcards.controller.v2.dto.FlashcardDto;
import com.grammr.flashcards.controller.v2.dto.SyncResultDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.grammr.domain.entity.Flashcard.Status.CREATED;
import static com.grammr.domain.entity.Flashcard.Status.MARKED_FOR_DELETION;
import static com.grammr.domain.entity.Flashcard.Status.SYNCED;
import static com.grammr.domain.entity.Flashcard.Status.UPDATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class SyncIntegrationTest extends IntegrationTestBase {

  @Test
  @SneakyThrows
  void shouldReturnOnlySyncableFlashcards() {
    // Given four flashcards with different statuses
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());
    var auth = createUserAuthentication(user);

    List.of(CREATED, UPDATED, SYNCED, MARKED_FOR_DELETION)
        .forEach(status -> flashcardRepository.save(
            FlashcardSpec.withDeck(deck)
                .front("Flashcard with status: " + status)
                .status(status)
                .build()
        ));

    // When syncing the deck
    var response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/deck/%s/sync".formatted(deck.getDeckId()))
            .with(authentication(auth))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    // SYNCED flashcards should not be returned
    var responseContent = objectMapper.readValue(
        response.getResponse().getContentAsString(), new TypeReference<List<FlashcardDto>>() {
        });

    assertThat(responseContent).hasSize(3);
  }

  @Test
  @SneakyThrows
  void shouldConfirmSync() {
    // Given flashcards that were previously synced
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());

    Map<Flashcard.Status, Flashcard> flashcards = Stream.of(CREATED, UPDATED, SYNCED, MARKED_FOR_DELETION)
        .map(status -> flashcardRepository.save(
            FlashcardSpec.withDeck(deck)
                .front("Flashcard with status: " + status)
                .status(status)
                .build()
        )).collect(Collectors.toMap(Flashcard::getStatus, flashcard -> flashcard));

    var authentication = createUserAuthentication(user);

    // When confirming the sync
    var syncConfirmation = new SyncResultDto(
        // Successful syncs
        List.of(
            flashcards.get(CREATED).getFlashcardId(),
            flashcards.get(UPDATED).getFlashcardId(),
            flashcards.get(MARKED_FOR_DELETION).getFlashcardId()
        ),
        // Failed syncs
        List.of()
    );

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/deck/%s/sync/confirm".formatted(deck.getDeckId()))
            .with(authentication(authentication))
            .content(objectMapper.writeValueAsString(syncConfirmation))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    // Then the statuses of the flashcards should be updated accordingly,
    // and the deleted flashcard should be removed from the repository
    assertThat(flashcardRepository.findById(flashcards.get(CREATED).getId()))
        .isPresent()
        .get()
        .extracting(Flashcard::getStatus)
        .isEqualTo(SYNCED);

    assertThat(flashcardRepository.findById(flashcards.get(UPDATED).getId()))
        .isPresent()
        .get()
        .extracting(Flashcard::getStatus)
        .isEqualTo(SYNCED);

    assertThat(flashcardRepository.findById(flashcards.get(MARKED_FOR_DELETION).getId()))
        .isEmpty();
  }
}

