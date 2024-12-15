package com.grammr.repository;

import com.grammr.domain.entity.Request;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {

  Optional<Request> findByRequestId(String requestId);
}
