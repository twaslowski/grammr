package com.grammr.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grammr.domain.enums.PartOfSpeechTag;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "flashcard")
public class Flashcard {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
  private long id;

  @NotNull
  private String question;

  @NotNull
  private String answer;

  @Enumerated(EnumType.STRING)
  private PartOfSpeechTag tokenPos;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "paradigm_id")
  private Paradigm paradim;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  @JsonIgnore
  private Deck deck;

  @CreationTimestamp
  @Column(updatable = false)
  private ZonedDateTime createdTimestamp;

  @UpdateTimestamp
  private ZonedDateTime updatedTimestamp;
}
