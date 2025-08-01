package com.grammr.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.entity.DeckSpec;
import com.grammr.domain.entity.FlashcardSpec;
import com.grammr.domain.entity.UserSpec;
import com.grammr.domain.enums.ExportDataType;
import com.grammr.flashcards.controller.dto.DeckCreationDto;
import com.grammr.flashcards.controller.dto.DeckExportDto;
import java.util.UUID;
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
    var creationDto = new DeckCreationDto("Test Deck", null);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/deck")
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
  void shouldRetrieveDeck() {
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());
    var auth = createUserAuthentication(user);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/deck")
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

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/deck/" + deck.getDeckId())
            .with(authentication(auth))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn();

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/deck/" + UUID.randomUUID())
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
            .content(objectMapper.writeValueAsString(new DeckExportDto(deck.getDeckId(), ExportDataType.APKG)))
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

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/deck/" + deck.getDeckId())
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andReturn();

    assertThat(deckRepository.findAll()).isEmpty();
    assertThat(flashcardRepository.findAll()).isEmpty();
  }
}
