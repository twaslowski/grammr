package com.grammr.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.entity.DeckSpec;
import com.grammr.domain.entity.Flashcard.Status;
import com.grammr.domain.entity.FlashcardSpec;
import com.grammr.domain.entity.UserSpec;
import com.grammr.flashcards.controller.v2.dto.FlashcardCreationDto;
import com.grammr.flashcards.controller.v2.dto.SyncDto;
import java.util.List;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@IntegrationTest
class FlashcardIntegrationTest extends IntegrationTestBase {

  @Test
  @SneakyThrows
  void shouldCreateFlashcard() {
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());
    var authentication = createUserAuthentication(user);

    var creationDto = new FlashcardCreationDto("Test Question", "Test Answer", null, null);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/deck/%s/flashcard".formatted(deck.getDeckId()))
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(creationDto)))
        .andExpect(status().is(201))
        .andExpect(jsonPath("$.id").isString())
        .andExpect(jsonPath("$.question").value("Test Question"))
        .andExpect(jsonPath("$.answer").value("Test Answer"))
        .andReturn();

    var flashcards = flashcardRepository.findByDeckId(deck.getId());
    assertThat(flashcards).hasSize(1);
    var flashcard = flashcards.getFirst();
    assertThat(flashcard.getFront()).isEqualTo("Test Question");
    assertThat(flashcard.getBack()).isEqualTo("Test Answer");
  }

  @Test
  @SneakyThrows
  void shouldNotCreateFlashcardIfExistsWithSameFrontAndDeck() {
    // Given
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());

    var existingFlashcard = FlashcardSpec.withDeck(deck)
        .front("What is the capital of France?")
        .back("Paris")
        .build();
    flashcardRepository.save(existingFlashcard);

    var flashcardCreationDto = new FlashcardCreationDto(
        existingFlashcard.getFront(),
        existingFlashcard.getBack(),
        null,
        null
    );

    var authentication = createUserAuthentication(user);

    // When
    mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/deck/%s/flashcard".formatted(deck.getDeckId()))
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(flashcardCreationDto)))
        .andExpect(status().isConflict())
        .andReturn();

    // Then
    assertThat(flashcardRepository.findAll()).hasSize(1);
  }

  @Test
  @SneakyThrows
  void shouldReturnSyncableFlashcards() {
    // Given
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());

    var createdFlashcard = FlashcardSpec.withDeck(deck)
        .front("What is the capital of France?")
        .status(Status.CREATED)
        .build();

    var exportedFlashcard = FlashcardSpec.withDeck(deck)
        .front("What is the capital of Germany?")
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
