package com.grammr.integration;

import static com.grammr.domain.entity.Flashcard.Type.BASIC;
import static com.grammr.domain.entity.Flashcard.Type.INFLECTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.entity.DeckSpec;
import com.grammr.domain.entity.FlashcardSpec;
import com.grammr.domain.entity.Paradigm;
import com.grammr.domain.entity.UserSpec;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.value.language.Inflection;
import com.grammr.flashcards.controller.v2.dto.FlashcardCreationDto;
import com.grammr.flashcards.controller.v2.dto.FlashcardDto;
import java.util.List;
import java.util.Set;
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

    var creationDto = new FlashcardCreationDto("Test Question", "Test Answer", BASIC, null, null);

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
  void shouldCreateFlashcardWithParadigm() {
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());
    var authentication = createUserAuthentication(user);

    var paradigm = paradigmRepository.save(Paradigm.builder()
        .partOfSpeech(PartOfSpeechTag.NOUN)
        .languageCode(LanguageCode.DE)
        .inflections(List.of(
            new Inflection("nominative", "singular", Set.of()),
            new Inflection("genitive", "singular", Set.of())
        ))
        .lemma("test")
        .build());

    var creationDto = new FlashcardCreationDto("Test Question", "Test Answer", INFLECTION, PartOfSpeechTag.NOUN, paradigm.getId());

    var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/deck/%s/flashcard".formatted(deck.getDeckId()))
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(creationDto)))
        .andExpect(status().is(201))
        .andReturn();

    var flashcardDto = objectMapper.readValue(result.getResponse().getContentAsString(), FlashcardDto.class);

    assertThat(flashcardDto.question()).isEqualTo("Test Question");
    assertThat(flashcardDto.answer()).isEqualTo("Test Answer");
    assertThat(flashcardDto.paradigm().paradigmId()).isEqualTo(paradigm.getId().toString());
    assertThat(flashcardDto.paradigm().partOfSpeech()).isEqualTo(paradigm.getPartOfSpeech());

    var flashcards = flashcardRepository.findByDeckId(deck.getId());

    assertThat(flashcards).hasSize(1);
    var flashcard = flashcards.getFirst();
    assertThat(flashcard.getFront()).isEqualTo("Test Question");
    assertThat(flashcard.getBack()).isEqualTo("Test Answer");
    assertThat(flashcard.getParadigm()).isNotNull();
    assertThat(flashcard.getParadigm().getId()).isEqualTo(paradigm.getId());
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
        BASIC,
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
    var updatePayload = new FlashcardCreationDto("Updated Front", "Updated Back", BASIC, null, null);

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
    var updatePayload = new FlashcardCreationDto("Updated Front", "Updated Back", BASIC, null, null);

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

  @Test
  @SneakyThrows
  void shouldRetrieveFlashcardsWithoutPagination() {
    // Given
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());
    var authentication = createUserAuthentication(user);

    // Create 5 flashcards
    for (int i = 1; i <= 5; i++) {
      flashcardRepository.save(FlashcardSpec.withDeck(deck)
          .front("Question " + i)
          .back("Answer " + i)
          .build());
    }

    // When & Then
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/deck/%s/flashcard".formatted(deck.getDeckId()))
            .with(authentication(authentication)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(5))
        .andExpect(jsonPath("$[0].question").exists())
        .andExpect(jsonPath("$[0].answer").exists());
  }

  @Test
  @SneakyThrows
  void shouldRetrieveFlashcardsWithPaginationEnabled() {
    // Given
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());
    var authentication = createUserAuthentication(user);

    // Create 10 flashcards
    for (int i = 1; i <= 10; i++) {
      flashcardRepository.save(FlashcardSpec.withDeck(deck)
          .front("Question " + i)
          .back("Answer " + i)
          .build());
    }

    // When & Then
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/deck/%s/flashcard".formatted(deck.getDeckId()))
            .param("paginated", "true")
            .param("page", "0")
            .param("size", "5")
            .with(authentication(authentication)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(5))
        .andExpect(jsonPath("$.page").value(0))
        .andExpect(jsonPath("$.size").value(5))
        .andExpect(jsonPath("$.totalElements").value(10))
        .andExpect(jsonPath("$.totalPages").value(2))
        .andExpect(jsonPath("$.first").value(true))
        .andExpect(jsonPath("$.last").value(false))
        .andExpect(jsonPath("$.hasNext").value(true))
        .andExpect(jsonPath("$.hasPrevious").value(false));
  }

  @Test
  @SneakyThrows
  void shouldRetrieveSecondPageOfFlashcards() {
    // Given
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());
    var authentication = createUserAuthentication(user);

    // Create 10 flashcards
    for (int i = 1; i <= 10; i++) {
      flashcardRepository.save(FlashcardSpec.withDeck(deck)
          .front("Question " + i)
          .back("Answer " + i)
          .build());
    }

    // When & Then
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/deck/%s/flashcard".formatted(deck.getDeckId()))
            .param("paginated", "true")
            .param("page", "1")
            .param("size", "5")
            .with(authentication(authentication)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(5))
        .andExpect(jsonPath("$.page").value(1))
        .andExpect(jsonPath("$.size").value(5))
        .andExpect(jsonPath("$.totalElements").value(10))
        .andExpect(jsonPath("$.totalPages").value(2))
        .andExpect(jsonPath("$.first").value(false))
        .andExpect(jsonPath("$.last").value(true))
        .andExpect(jsonPath("$.hasNext").value(false))
        .andExpect(jsonPath("$.hasPrevious").value(true));
  }

  @Test
  @SneakyThrows
  void shouldSortFlashcardsAscending() {
    // Given
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());
    var authentication = createUserAuthentication(user);

    // Create flashcards with different front text for sorting
    flashcardRepository.save(FlashcardSpec.withDeck(deck)
        .front("Charlie")
        .back("Answer C")
        .build());
    flashcardRepository.save(FlashcardSpec.withDeck(deck)
        .front("Alpha")
        .back("Answer A")
        .build());
    flashcardRepository.save(FlashcardSpec.withDeck(deck)
        .front("Beta")
        .back("Answer B")
        .build());

    // When & Then
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/deck/%s/flashcard".formatted(deck.getDeckId()))
            .param("paginated", "true")
            .param("sortBy", "front")
            .param("sortDirection", "asc")
            .with(authentication(authentication)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].question").value("Alpha"))
        .andExpect(jsonPath("$.content[1].question").value("Beta"))
        .andExpect(jsonPath("$.content[2].question").value("Charlie"));
  }

  @Test
  @SneakyThrows
  void shouldSortFlashcardsDescending() {
    // Given
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());
    var authentication = createUserAuthentication(user);

    // Create flashcards with different front text for sorting
    flashcardRepository.save(FlashcardSpec.withDeck(deck)
        .front("Charlie")
        .back("Answer C")
        .build());
    flashcardRepository.save(FlashcardSpec.withDeck(deck)
        .front("Alpha")
        .back("Answer A")
        .build());
    flashcardRepository.save(FlashcardSpec.withDeck(deck)
        .front("Beta")
        .back("Answer B")
        .build());

    // When & Then
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/deck/%s/flashcard".formatted(deck.getDeckId()))
            .param("paginated", "true")
            .param("sortBy", "front")
            .param("sortDirection", "desc")
            .with(authentication(authentication)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].question").value("Charlie"))
        .andExpect(jsonPath("$.content[1].question").value("Beta"))
        .andExpect(jsonPath("$.content[2].question").value("Alpha"));
  }

  @Test
  @SneakyThrows
  void shouldReturnEmptyPageWhenNoFlashcardsExist() {
    // Given
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());
    var authentication = createUserAuthentication(user);

    // When & Then
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/deck/%s/flashcard".formatted(deck.getDeckId()))
            .param("paginated", "true")
            .param("page", "0")
            .param("size", "10")
            .with(authentication(authentication)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(0))
        .andExpect(jsonPath("$.page").value(0))
        .andExpect(jsonPath("$.size").value(10))
        .andExpect(jsonPath("$.totalElements").value(0))
        .andExpect(jsonPath("$.totalPages").value(0))
        .andExpect(jsonPath("$.first").value(true))
        .andExpect(jsonPath("$.last").value(true))
        .andExpect(jsonPath("$.hasNext").value(false))
        .andExpect(jsonPath("$.hasPrevious").value(false));
  }

  @Test
  @SneakyThrows
  void shouldHandlePageSizeGreaterThanTotalElements() {
    // Given
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var deck = deckRepository.save(DeckSpec.withUser(user).build());
    var authentication = createUserAuthentication(user);

    // Create 3 flashcards
    for (int i = 1; i <= 3; i++) {
      flashcardRepository.save(FlashcardSpec.withDeck(deck)
          .front("Question " + i)
          .back("Answer " + i)
          .build());
    }

    // When & Then - request page size of 10 when only 3 elements exist
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/deck/%s/flashcard".formatted(deck.getDeckId()))
            .param("paginated", "true")
            .param("page", "0")
            .param("size", "10")
            .with(authentication(authentication)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(3))
        .andExpect(jsonPath("$.page").value(0))
        .andExpect(jsonPath("$.size").value(10))
        .andExpect(jsonPath("$.totalElements").value(3))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.first").value(true))
        .andExpect(jsonPath("$.last").value(true));
  }

  @Test
  @SneakyThrows
  void shouldReturnNotFoundForNonExistentDeck() {
    // Given
    var user = userRepository.save(UserSpec.validWithoutId().build());
    var authentication = createUserAuthentication(user);
    var nonExistentDeckId = UUID.randomUUID();

    // When & Then
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/deck/%s/flashcard".formatted(nonExistentDeckId))
            .param("paginated", "true")
            .with(authentication(authentication)))
        .andExpect(status().isNotFound());
  }
}
