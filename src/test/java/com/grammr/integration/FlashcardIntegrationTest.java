package com.grammr.integration;

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
import java.util.List;
import java.util.Set;
import java.util.UUID;
import com.grammr.flashcards.controller.v2.dto.FlashcardDto;
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

    var creationDto = new FlashcardCreationDto("Test Question", "Test Answer", PartOfSpeechTag.NOUN, paradigm.getId());

    var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/deck/%s/flashcard".formatted(deck.getDeckId()))
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(creationDto)))
        .andExpect(status().is(201))
        .andReturn();

    var flashcardDto = objectMapper.readValue(result.getResponse().getContentAsString(), FlashcardDto.class);

    assertThat(flashcardDto.question()).isEqualTo("Test Question");
    assertThat(flashcardDto.answer()).isEqualTo("Test Answer");
    assertThat(flashcardDto.tokenPos()).isEqualTo(PartOfSpeechTag.NOUN);
    assertThat(flashcardDto.type()).isEqualTo(INFLECTION);
    assertThat(flashcardDto.paradigmId()).isEqualTo(paradigm.getId().toString());

    var flashcards = flashcardRepository.findByDeckId(deck.getId());

    assertThat(flashcards).hasSize(1);
    var flashcard = flashcards.getFirst();
    assertThat(flashcard.getFront()).isEqualTo("Test Question");
    assertThat(flashcard.getBack()).isEqualTo("Test Answer");
    assertThat(flashcard.getParadigm()).isNotNull();
    assertThat(flashcard.getParadigm().getId()).isEqualTo(paradigm.getId());
    assertThat(flashcard.getType()).isEqualTo(INFLECTION);
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
