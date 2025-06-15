package com.grammr.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String externalId;

  @NotNull
  private boolean isAnonymous;

  private UUID sessionId;

  @NotNull
  private boolean deleted;

  @JsonIgnore
  @CreationTimestamp
  @Column(updatable = false)
  private ZonedDateTime createdTimestamp;

  @JsonIgnore
  @UpdateTimestamp
  private ZonedDateTime updatedTimestamp;

  public static User fromExternalId(String externalId) {
    return User.builder()
        .deleted(false)
        .externalId(externalId)
        .build();
  }

  public static User anonymous(String sessionId) {
    return User.builder()
        .isAnonymous(true)
        .deleted(false)
        .sessionId(UUID.fromString(sessionId))
        .build();
  }
}
