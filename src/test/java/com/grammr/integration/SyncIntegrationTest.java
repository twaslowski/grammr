package com.grammr.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.entity.DeckSpec;
import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.entity.FlashcardSpec;
import com.grammr.domain.entity.UserSpec;
import com.grammr.flashcards.controller.v2.dto.FlashcardSyncActionDto;
import com.grammr.flashcards.controller.v2.dto.SyncResultDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.grammr.flashcards.controller.v2.dto.SyncAction.CREATE;
import static com.grammr.flashcards.controller.v2.dto.SyncAction.DELETE;
import static com.grammr.flashcards.controller.v2.dto.SyncAction.UPDATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class SyncIntegrationTest extends IntegrationTestBase {

  @SneakyThrows
  @ParameterizedTest
  @EnumSource(value = Flashcard.Status.class,
      names = {"CREATION_INITIATED", "CREATION_SUCCEEDED", "CREATION_FAILED",
          "DELETION_INITIATED", "DELETION_FAILED",
          "UPDATE_INITIATED", "UPDATE_SUCCEEDED", "UPDATE_FAILED"}
  )
  void shouldReturnOnlySyncableFlashcards(Flashcard.Status status) {
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());
    var auth = createUserAuthentication(user);

    var createdFlashcard = FlashcardSpec.withDeck(deck)
        .front("Create me")
        .status(Flashcard.Status.CREATED)
        .build();

    var updatedFlashcard = FlashcardSpec.withDeck(deck)
        .front("Update me")
        .status(Flashcard.Status.UPDATED)
        .build();

    var deletedFlashcard = FlashcardSpec.withDeck(deck)
        .front("Delete me")
        .status(Flashcard.Status.MARKED_FOR_DELETION)
        .build();

    var exportedFlashcard = FlashcardSpec.withDeck(deck)
        .front("Do not sync me")
        .status(status)
        .build();

    flashcardRepository.saveAll(
        List.of(createdFlashcard, exportedFlashcard, updatedFlashcard, deletedFlashcard)
    );

    var response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/deck/%s/sync".formatted(deck.getDeckId()))
            .with(authentication(auth))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    var responseContent = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<List<FlashcardSyncActionDto>>() {
    });

    assertThat(responseContent).hasSize(3);

    assertThat(flashcardRepository.findById(createdFlashcard.getId()))
        .isPresent()
        .get()
        .extracting(Flashcard::getStatus)
        .isEqualTo(Flashcard.Status.CREATION_INITIATED);

    assertThat(flashcardRepository.findById(updatedFlashcard.getId()))
        .isPresent()
        .get()
        .extracting(Flashcard::getStatus)
        .isEqualTo(Flashcard.Status.UPDATE_INITIATED);

    assertThat(flashcardRepository.findById(deletedFlashcard.getId()))
        .isPresent()
        .get()
        .extracting(Flashcard::getStatus)
        .isEqualTo(Flashcard.Status.DELETION_INITIATED);
  }

  @Test
  @SneakyThrows
  void shouldConfirmSync() {
    // Given
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());

    var createdFlashcard = FlashcardSpec.withDeck(deck)
        .status(Flashcard.Status.CREATION_INITIATED)
        .front("Create me")
        .build();

    var updatedFlashcard = FlashcardSpec.withDeck(deck)
        .status(Flashcard.Status.UPDATE_INITIATED)
        .front("Update me")
        .build();

    var deletedFlashcard = FlashcardSpec.withDeck(deck)
        .status(Flashcard.Status.DELETION_INITIATED)
        .front("Delete me")
        .build();

    var authentication = createUserAuthentication(user);
    flashcardRepository.saveAll(List.of(createdFlashcard, updatedFlashcard, deletedFlashcard));

    var syncConfirmation = new SyncResultDto(
        List.of(createdFlashcard.getFlashcardId(), deletedFlashcard.getFlashcardId()),
        List.of(updatedFlashcard.getFlashcardId())
    );

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/deck/%s/sync/confirm".formatted(deck.getDeckId()))
            .with(authentication(authentication))
            .content(objectMapper.writeValueAsString(syncConfirmation))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    assertThat(flashcardRepository.findById(createdFlashcard.getId()))
        .isPresent()
        .get()
        .extracting(Flashcard::getStatus)
        .isEqualTo(Flashcard.Status.CREATION_SUCCEEDED);

    assertThat(flashcardRepository.findById(updatedFlashcard.getId()))
        .isPresent()
        .get()
        .extracting(Flashcard::getStatus)
        .isEqualTo(Flashcard.Status.UPDATE_FAILED);

    assertThat(flashcardRepository.findById(deletedFlashcard.getId()))
        .isEmpty();
  }
}

