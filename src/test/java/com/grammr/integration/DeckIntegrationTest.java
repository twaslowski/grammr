package com.grammr.integration;

import static com.grammr.domain.entity.Flashcard.Status.CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.entity.DeckSpec;
import com.grammr.domain.entity.Flashcard.Status;
import com.grammr.domain.entity.FlashcardSpec;
import com.grammr.domain.entity.UserSpec;
import java.util.UUID;
import com.grammr.flashcards.controller.v2.dto.DeckUpdateDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@IntegrationTest
class DeckIntegrationTest extends IntegrationTestBase {

  @Test
  @SneakyThrows
  void shouldCreateDeck() {
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var auth = createUserAuthentication(user);
    var creationDto = new com.grammr.flashcards.controller.v2.dto.DeckCreationDto("Test Deck", null);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/deck")
            .with(authentication(auth))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(creationDto)))
        .andExpect(status().is(201))
        .andExpect(jsonPath("$.id").isString())
        .andExpect(jsonPath("$.name").value("Test Deck"))
        .andReturn();

    assertThat(deckRepository.findAll()).hasSize(1);
  }

  @Test
  @SneakyThrows
  void shouldRetrieveDecks() {
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());
    var auth = createUserAuthentication(user);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/deck")
            .with(authentication(auth))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(deck.getDeckId().toString()))
        .andReturn();
  }

  @Test
  @SneakyThrows
  void shouldReturnNotFoundForNonExistentOrNotOwnedDeck() {
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());

    var otherUser = userRepository.save(UserSpec.validWithoutId()
        .externalId("some-other-id")
        .build());
    var auth = createUserAuthentication(otherUser);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/deck/" + deck.getDeckId())
            .with(authentication(auth))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn();

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/deck/" + UUID.randomUUID())
            .with(authentication(auth))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn();
  }

  @Test
  @SneakyThrows
  void shouldExportDeck() {
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());
    flashcardRepository.save(FlashcardSpec.withDeck(deck).build());

    var authentication = createUserAuthentication(user);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/deck/%s/export".formatted(deck.getDeckId()))
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_OCTET_STREAM))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
        .andReturn();
  }

  @Test
  @SneakyThrows
  void shouldDeleteDeckCascadingFlashcards() {
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());
    var authentication = createUserAuthentication(user);

    flashcardRepository.save(FlashcardSpec.withDeck(deck).build());

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/v2/deck/" + deck.getDeckId())
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andReturn();

    assertThat(deckRepository.findAll()).isEmpty();
    assertThat(flashcardRepository.findAll()).isEmpty();
  }

  @Test
  @SneakyThrows
  void shouldUpdateDeckProperties() {
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).name("Original Name").description("Original Description").build());
    var authentication = createUserAuthentication(user);
    var updateDto = new DeckUpdateDto("Updated Name", "Updated Description");

    mockMvc.perform(MockMvcRequestBuilders.put("/api/v2/deck/" + deck.getDeckId())
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(deck.getDeckId().toString()))
        .andExpect(jsonPath("$.name").value("Updated Name"))
        .andExpect(jsonPath("$.description").value("Updated Description"))
        .andReturn();

    // Verify the deck was actually updated in the database
    var updatedDeck = deckRepository.findById(deck.getId()).orElseThrow();
    assertThat(updatedDeck.getName()).isEqualTo("Updated Name");
    assertThat(updatedDeck.getDescription()).isEqualTo("Updated Description");
  }

  @Test
  @SneakyThrows
  void shouldPartiallyUpdateDeck() {
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).name("Original Name").description("Original Description").build());
    var authentication = createUserAuthentication(user);

    // Update only the name
    var nameUpdateDto = new DeckUpdateDto("Updated Name", null);

    mockMvc.perform(MockMvcRequestBuilders.put("/api/v2/deck/" + deck.getDeckId())
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(nameUpdateDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Updated Name"))
        .andExpect(jsonPath("$.description").value("Original Description"));

    // Update only the description
    var descriptionUpdateDto = new DeckUpdateDto(null, "Updated Description");

    mockMvc.perform(MockMvcRequestBuilders.put("/api/v2/deck/" + deck.getDeckId())
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(descriptionUpdateDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Updated Name"))
        .andExpect(jsonPath("$.description").value("Updated Description"));
  }

  @Test
  @SneakyThrows
  void shouldResetSyncStatus() {
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());
    var authentication = createUserAuthentication(user);

    flashcardRepository.save(FlashcardSpec.withDeck(deck).front("some-front").status(Status.SYNCED).build());
    flashcardRepository.save(FlashcardSpec.withDeck(deck).front("some-other-front").status(Status.UPDATED).build());

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/deck/%s/reset-sync".formatted(deck.getDeckId()))
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andReturn();

    var flashcards = flashcardRepository.findAll();
    assertThat(flashcards).hasSize(2);
    assertThat(flashcards).allMatch(f -> f.getStatus() == CREATED);
  }
}
