package com.grammr.repository;

import com.grammr.domain.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByExternalId(String externalId);

  Optional<User> findBySessionId(UUID sessionId);
}
