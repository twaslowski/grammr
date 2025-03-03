package com.grammr.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.entity.Deck;
import com.grammr.domain.entity.Flashcard;
import com.grammr.domain.entity.User;
import com.grammr.domain.entity.UserSpec;
import com.grammr.domain.enums.ExportDataType;
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
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
@AutoConfigureMockMvc
public class AnkiIntegrationTest extends IntegrationTestBase {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @SneakyThrows
  void shouldReturnUnauthorized() {
    var creationDto = new AnkiDeckCreationDto("Test Deck", null);
    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/anki/deck")
            .with(anonymous())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(creationDto)))
        .andExpect(status().is(401))
        .andReturn();
  }

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
  void shouldCreateFlashcard() {
    var user = userRepository.save(UserSpec.valid().build());
    var deck = deckRepository.save(Deck.builder().name("Test Deck").user(user).build());
    var authentication = createUserAuthentication(user);
    var creationDto = new AnkiFlashcardCreationDto(deck.getId(), "Test Question", "Test Answer");
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
