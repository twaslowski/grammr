package com.grammr.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @NotNull
  private String externalId;

  @JsonIgnore
  @CreationTimestamp
  @Column(updatable = false)
  private ZonedDateTime createdTimestamp;

  @JsonIgnore
  @UpdateTimestamp
  private ZonedDateTime updatedTimestamp;

  public static User fromExternalId(String externalId) {
    return User.builder()
        .externalId(externalId)
        .build();
  }
}
