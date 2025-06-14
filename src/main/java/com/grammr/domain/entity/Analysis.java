package com.grammr.domain.entity;

import static org.hibernate.type.SqlTypes.JSON;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.value.language.Token;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "analysis")
public class Analysis {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analysis_id_seq")
  private Long id;

  @NotNull
  private UUID analysisId;

  @NotNull
  private String phrase;

  @NotNull
  @Enumerated(EnumType.STRING)
  private LanguageCode sourceLanguage;

  @Enumerated(EnumType.STRING)
  private LanguageCode targetLanguage;

  @JdbcTypeCode(value = JSON)
  private List<Token> analysedTokens;

  @CreationTimestamp
  @Column(updatable = false)
  private ZonedDateTime createdTimestamp;

  @UpdateTimestamp
  private ZonedDateTime updatedTimestamp;

  public Analysis updateTokensWith(Token token) {
    if (token.index() >= analysedTokens.size()) {
      throw new IllegalArgumentException("Token not contained in analysis: " + token.index());
    }

    var sorted = new ArrayList<>(analysedTokens.stream()
        .sorted(Comparator.comparing(Token::index))
        .toList());

    sorted.set((int) token.index(), token);

    this.analysedTokens = sorted;
    return this;
  }

  public static Analysis from(String phrase, LanguageCode languageCode, List<Token> analysedTokens) {
    return Analysis.builder()
        .phrase(phrase)
        .analysisId(UUID.randomUUID())
        .sourceLanguage(languageCode)
        .analysedTokens(analysedTokens)
        .build();
  }
}
