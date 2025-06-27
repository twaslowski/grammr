package com.grammr.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.entity.DeckSpec;
import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.entity.Flashcard.Status;
import com.grammr.domain.entity.UserSpec;
import com.grammr.domain.enums.ExportDataType;
import com.grammr.flashcards.controller.dto.DeckCreationDto;
import com.grammr.flashcards.controller.dto.DeckExportDto;
import com.grammr.flashcards.controller.dto.FlashcardCreationDto;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@IntegrationTest
public class AnkiIntegrationTest extends IntegrationTestBase {

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
  @Disabled
  void shouldSyncNonExportedCards() {
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());
    var auth = createUserAuthentication(user);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/deck/sync")
            .with(authentication(auth))
            .content(objectMapper.writeValueAsString(new DeckExportDto(deck.getDeckId(), null)))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(deck.getId()))
        .andExpect(jsonPath("$.name").value(deck.getName()))
        .andExpect(jsonPath("$.flashcards").isArray())
        .andExpect(jsonPath("$.flashcards", hasSize(1)))
        .andReturn();

    // Status of the exported Flashcard now also is EXPORTED
    assertThat(flashcardRepository.findAll()).hasSize(2);
    assertThat(flashcardRepository.findByDeckIdAndStatusNot(deck.getId(), Status.EXPORTED)).hasSize(0);
  }

  @Test
  @SneakyThrows
  void shouldCreateFlashcard() {
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());
    var authentication = createUserAuthentication(user);

    var creationDto = new FlashcardCreationDto(deck.getDeckId(), "Test Question", "Test Answer", null, null);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/flashcard")
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(creationDto)))
        .andExpect(status().is(201))
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.question").value("Test Question"))
        .andExpect(jsonPath("$.answer").value("Test Answer"))
        .andReturn();

    var flashcards = flashcardRepository.findByDeckId(deck.getId());
    assertThat(flashcards).hasSize(1);
    var flashcard = flashcards.getFirst();
    assertThat(flashcard.getQuestion()).isEqualTo("Test Question");
    assertThat(flashcard.getAnswer()).isEqualTo("Test Answer");
  }

  @Test
  @SneakyThrows
  void shouldExportDeck() {
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());
    var authentication = createUserAuthentication(user);
    flashcardRepository.save(Flashcard.builder()
        .question("Question")
        .answer("Answer")
        .status(Status.CREATED)
        .deck(deck)
        .build()
    );

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/deck/export")
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

    flashcardRepository.save(Flashcard.builder()
        .question("Question")
        .answer("Answer")
        .status(Status.CREATED)
        .deck(deck)
        .build()
    );

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/deck/" + deck.getDeckId())
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andReturn();

    assertThat(deckRepository.findAll()).isEmpty();
    assertThat(flashcardRepository.findAll()).isEmpty();
  }
}
