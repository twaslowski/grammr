package com.grammr.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
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
@Table(name = "deck")
public class Deck {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deck_id_seq")
  private long id;

  @NotNull
  private String name;

  @JsonIgnore
  @CreationTimestamp
  @Column(updatable = false)
  private ZonedDateTime createdTimestamp;

  @JsonIgnore
  @UpdateTimestamp
  private ZonedDateTime updatedTimestamp;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn
  private User user;
}
