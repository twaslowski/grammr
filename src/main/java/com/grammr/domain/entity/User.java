package com.grammr.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grammr.repository.converter.EmailEncryptionConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "app_user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
  private long id;

  @NotNull
  private String username;

  @NotNull
  @Convert(converter = EmailEncryptionConverter.class)
  @JsonIgnore
  private String email;

  @NotNull
  @JsonIgnore
  private String emailHash;

  @NotNull
  @JsonIgnore
  private String password;

  @JsonIgnore
  @CreationTimestamp
  @Column(updatable = false)
  private ZonedDateTime createdTimestamp;

  @JsonIgnore
  @UpdateTimestamp
  private ZonedDateTime updatedTimestamp;
}
