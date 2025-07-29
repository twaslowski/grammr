package com.grammr.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.entity.DeckSpec;
import com.grammr.domain.entity.Flashcard.Status;
import com.grammr.domain.entity.FlashcardSpec;
import com.grammr.domain.entity.UserSpec;
import com.grammr.flashcards.controller.v2.dto.SyncDto;
import java.util.List;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@IntegrationTest
public class FlashcardIntegrationTest extends IntegrationTestBase {

  @Test
  @SneakyThrows
  void shouldReturnSyncableFlashcards() {
    // Given
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());

    var createdFlashcard = FlashcardSpec.withDeck(deck)
        .status(Status.CREATED)
        .build();

    var exportedFlashcard = FlashcardSpec.withDeck(deck)
        .status(Status.EXPORT_COMPLETED)
        .build();

    var authentication = createUserAuthentication(user);
    flashcardRepository.saveAll(List.of(createdFlashcard, exportedFlashcard));

    var response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/deck/%s/sync".formatted(deck.getDeckId()))
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    var responseContent = objectMapper.readValue(response.getResponse().getContentAsString(), SyncDto.class);

    assertThat(responseContent.syncId()).isNotNull();
    assertThat(responseContent.flashcards()).hasSize(1);
  }

  @Test
  @SneakyThrows
  void shouldConfirmSync() {
    // Given
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());

    var syncId = UUID.randomUUID();
    var createdFlashcard = FlashcardSpec.withDeck(deck)
        .status(Status.EXPORT_INITIATED)
        .syncId(syncId)
        .build();

    var authentication = createUserAuthentication(user);
    flashcardRepository.saveAll(List.of(createdFlashcard));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/deck/%s/sync/%s/confirm".formatted(deck.getDeckId(), syncId))
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    var storedFlashcard = flashcardRepository.findById(createdFlashcard.getId())
        .orElseThrow(() -> new IllegalStateException("Flashcard not found"));
    assertThat(storedFlashcard.getStatus()).isEqualTo(Status.EXPORT_COMPLETED);
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestForBadSyncId() {
    // Given
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());

    var syncId = UUID.randomUUID();
    var createdFlashcard = FlashcardSpec.withDeck(deck)
        .status(Status.EXPORT_INITIATED)
        .syncId(syncId)
        .build();

    var authentication = createUserAuthentication(user);
    flashcardRepository.saveAll(List.of(createdFlashcard));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/deck/%s/sync/%s/confirm".formatted(deck.getDeckId(), UUID.randomUUID()))
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn();
  }
}
