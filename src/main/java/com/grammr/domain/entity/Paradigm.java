package com.grammr.domain.entity;

import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.value.language.Inflection;
import com.grammr.domain.value.language.ParadigmDTO;
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
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "paradigm")
public class Paradigm {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotNull
  @Enumerated(EnumType.STRING)
  private PartOfSpeechTag partOfSpeech;

  @NotNull
  private String lemma;

  @NotNull
  @Enumerated(EnumType.STRING)
  private LanguageCode languageCode;

  @JdbcTypeCode(SqlTypes.JSON)
  private List<Inflection> inflections;

  @CreationTimestamp
  @Column(updatable = false)
  private ZonedDateTime createdAt;

  @UpdateTimestamp
  private ZonedDateTime updatedAt;

  public static Paradigm from(ParadigmDTO paradigmDTO) {
    return Paradigm.builder()
        .partOfSpeech(paradigmDTO.partOfSpeech())
        .lemma(paradigmDTO.lemma())
        .languageCode(paradigmDTO.language())
        .inflections(paradigmDTO.inflections())
        .build();
  }

  public ParadigmDTO toDTO() {
    return new ParadigmDTO(partOfSpeech, lemma, languageCode, inflections);
  }
}
