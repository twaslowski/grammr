package com.grammr.repository;

import com.grammr.domain.entity.Analysis;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisRepository extends JpaRepository<Analysis, Long> {

  Optional<Analysis> findByAnalysisId(UUID analysisId);

  boolean existsByAnalysisId(UUID analysisId);
}
