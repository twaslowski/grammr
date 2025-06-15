package com.grammr.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.entity.User;
import com.grammr.repository.DeckRepository;
import com.grammr.repository.FlashcardRepository;
import com.grammr.repository.ParadigmRepository;
import com.grammr.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class IntegrationTestBase {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected DeckRepository deckRepository;

  @Autowired
  protected FlashcardRepository flashcardRepository;

  @Autowired
  protected ParadigmRepository paradigmRepository;

  @BeforeEach
  public void setUp() {
    flashcardRepository.deleteAll();
    deckRepository.deleteAll();
    userRepository.deleteAll();
    paradigmRepository.deleteAll();
  }

  protected Authentication createUserAuthentication(User user) {
    return new UsernamePasswordAuthenticationToken(user, null, List.of(
        new SimpleGrantedAuthority("ROLE_USER")
    ));
  }
}
