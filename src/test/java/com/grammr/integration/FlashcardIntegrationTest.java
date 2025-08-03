package com.grammr.integration;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.entity.DeckSpec;
import com.grammr.domain.entity.FlashcardSpec;
import com.grammr.domain.entity.UserSpec;
import com.grammr.flashcards.controller.v2.dto.FlashcardCreationDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
  void shouldReturnNotFoundWhenUpdatingNonExistentFlashcard() {
    // Given
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());
    var authentication = createUserAuthentication(user);
    var nonExistentFlashcardId = UUID.randomUUID();
    var updatePayload = new FlashcardCreationDto("Updated Front", "Updated Back", null, null);

    // When & Then
    mockMvc.perform(MockMvcRequestBuilders.put("/api/v2/deck/%s/flashcard/%s".formatted(deck.getDeckId(), nonExistentFlashcardId))
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatePayload)))
        .andExpect(status().isNotFound());
  }

  @Test
  @SneakyThrows
  void shouldUpdateFlashcardSuccessfully() {
    // Given
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());
    var flashcard = flashcardRepository.save(FlashcardSpec.withDeck(deck).build());
    var authentication = createUserAuthentication(user);
    var updatePayload = new FlashcardCreationDto("Updated Front", "Updated Back", null, null);


    // When & Then
    mockMvc.perform(MockMvcRequestBuilders.put("/api/v2/deck/%s/flashcard/%s".formatted(deck.getDeckId(), flashcard.getFlashcardId()))
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatePayload)))
        .andExpect(status().isOk());

    // Verify update
    var updated = flashcardRepository.findById(flashcard.getId()).orElseThrow();
    assertThat(updated.getFront()).isEqualTo(updatePayload.question());
    assertThat(updated.getBack()).isEqualTo(updatePayload.answer());
  }
}
