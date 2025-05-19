package com.grammr.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.entity.Deck;
import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.entity.Flashcard.Status;
import com.grammr.domain.entity.Paradigm;
import com.grammr.domain.entity.User;
import com.grammr.domain.entity.UserSpec;
import com.grammr.domain.enums.ExportDataType;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.port.dto.anki.AnkiDeckCreationDto;
import com.grammr.port.dto.anki.AnkiFlashcardCreationDto;
import com.grammr.port.dto.anki.InboundAnkiDeckExportDto;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@IntegrationTest
@AutoConfigureMockMvc
public class AnkiIntegrationTest extends IntegrationTestBase {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @SneakyThrows
  void shouldCreateDeck() {
    var auth = createUserAuthentication();
    var creationDto = new AnkiDeckCreationDto("Test Deck", null);
    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/anki/deck")
            .with(authentication(auth))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(creationDto)))
        .andExpect(status().is(201))
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.name").value("Test Deck"))
        .andReturn();

    assertThat(deckRepository.findAll()).hasSize(1);
  }

  @Test
  @SneakyThrows
  void shouldRetrieveDeckWithFlashcards() {
    var user = userRepository.save(UserSpec.valid().build());
    var deck = deckRepository.save(Deck.builder().name("Test Deck").user(user).build());
    var paradigm = paradigmRepository.save(Paradigm.builder()
        .lemma("Hund")
        .languageCode(LanguageCode.DE)
        .partOfSpeech(PartOfSpeechTag.NOUN)
        .inflections(List.of())
        .build());
    var flashcard = flashcardRepository.save(Flashcard.builder()
        .question("Dog")
        .answer("Hund")
        .tokenPos(PartOfSpeechTag.NOUN)
        .paradigm(paradigm)
        .deck(deck)
        .status(Status.CREATED)
        .build());
    var auth = createUserAuthentication(user);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/anki/deck")
            .with(authentication(auth))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(deck.getId()))
        .andExpect(jsonPath("$[0].flashcards").isArray())
        .andExpect(jsonPath("$[0].flashcards.[0].id").value(flashcard.getId()))
        .andReturn();
  }

  @Test
  @SneakyThrows
  void shouldExportNonExportedCards() {
    var user = userRepository.save(UserSpec.valid().build());
    var deck = deckRepository.save(Deck.builder().name("Test Deck").user(user).build());

    var paradigm = paradigmRepository.save(Paradigm.builder()
        .lemma("Hund")
        .languageCode(LanguageCode.DE)
        .partOfSpeech(PartOfSpeechTag.NOUN)
        .inflections(List.of())
        .build());
    var flashcard = flashcardRepository.save(Flashcard.builder()
        .question("Dog")
        .answer("Hund")
        .tokenPos(PartOfSpeechTag.NOUN)
        .paradigm(paradigm)
        .deck(deck)
        .status(Status.CREATED)
        .build());

    flashcardRepository.save(Flashcard.builder()
        .question("I go on a walk with the dog")
        .answer("Ich gehe mit dem Hund spazieren")
        .deck(deck)
        .status(Status.EXPORTED)
        .build());

    var auth = createUserAuthentication(user);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/anki/sync")
            .with(authentication(auth))
            .content(objectMapper.writeValueAsString(new InboundAnkiDeckExportDto(deck.getId(), null)))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(deck.getId()))
        .andExpect(jsonPath("$.name").value(deck.getName()))
        .andExpect(jsonPath("$.flashcards").isArray())
        .andExpect(jsonPath("$.flashcards", hasSize(1)))
        .andExpect(jsonPath("$.flashcards.[0].id").value(flashcard.getId()))
        .andReturn();

    // Status of the exported Flashcard now also is EXPORTED
    assertThat(flashcardRepository.findAll()).hasSize(2);
    assertThat(flashcardRepository.findByDeckIdAndStatusNot(deck.getId(), Status.EXPORTED)).hasSize(0);
  }

  @Test
  @SneakyThrows
  void shouldCreateFlashcard() {
    var user = userRepository.save(UserSpec.valid().build());
    var deck = deckRepository.save(Deck.builder().name("Test Deck").user(user).build());
    var authentication = createUserAuthentication(user);
    var creationDto = new AnkiFlashcardCreationDto(deck.getId(), "Test Question", "Test Answer", null, null);
    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/anki/flashcard")
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
    var user = userRepository.save(UserSpec.valid().build());
    var deck = deckRepository.save(Deck.builder().name("Test Deck").user(user).build());
    var authentication = createUserAuthentication(user);
    flashcardRepository.save(Flashcard.builder()
        .question("Question")
        .answer("Answer")
        .status(Status.CREATED)
        .deck(deck)
        .build()
    );

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/anki/export")
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new InboundAnkiDeckExportDto(deck.getId(), ExportDataType.APKG)))
            .accept(MediaType.APPLICATION_OCTET_STREAM))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
        .andReturn();
  }

  @Test
  @SneakyThrows
  void shouldDeleteDeck() {
    var user = userRepository.save(UserSpec.valid().build());
    var deck = deckRepository.save(Deck.builder().name("Test Deck").user(user).build());
    var authentication = createUserAuthentication(user);
    flashcardRepository.save(Flashcard.builder()
        .question("Question")
        .answer("Answer")
        .status(Status.CREATED)
        .deck(deck)
        .build()
    );

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/anki/deck/" + deck.getId())
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andReturn();

    assertThat(deckRepository.findAll()).isEmpty();
    assertThat(flashcardRepository.findAll()).isEmpty();
  }

  private Authentication createUserAuthentication() {
    var user = userRepository.save(UserSpec.valid().build());
    return new UsernamePasswordAuthenticationToken(user, null, List.of());
  }

  private Authentication createUserAuthentication(User user) {
    return new UsernamePasswordAuthenticationToken(user, null, List.of());
  }
}
