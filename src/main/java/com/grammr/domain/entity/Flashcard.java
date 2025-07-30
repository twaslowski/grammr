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

  public enum Status {
    CREATED,
    UPDATED,
    EXPORT_INITIATED,
    EXPORT_COMPLETED,
    EXPORT_FAILED
  }

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flashcard_id_seq")
  private long id;

  @NotNull
  private UUID flashcardId;

  @NotNull
  private String front;

  @NotNull
  private String back;

  @Enumerated(EnumType.STRING)
  private PartOfSpeechTag tokenPos;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "paradigm_id")
  private Paradigm paradigm;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  @JsonIgnore
  private Deck deck;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Status status;

  private UUID syncId;

  @CreationTimestamp
  @Column(updatable = false)
  private ZonedDateTime createdTimestamp;

  @UpdateTimestamp
  private ZonedDateTime updatedTimestamp;

  public Flashcard confirmSync() {
    this.status = Status.EXPORT_COMPLETED;
    return this;
  }

  public void initiateSync(UUID syncId) {
    this.status = Status.EXPORT_INITIATED;
    this.syncId = syncId;
  }
}
