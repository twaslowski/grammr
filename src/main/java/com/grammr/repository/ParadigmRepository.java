package com.grammr.repository;

import com.grammr.domain.entity.Paradigm;
import com.grammr.domain.enums.LanguageCode;
import com.grammr.domain.enums.PartOfSpeechTag;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParadigmRepository extends JpaRepository<Paradigm, UUID> {

  Optional<Paradigm> findByLemmaAndPartOfSpeechAndLanguageCode(String lemma, PartOfSpeechTag partOfSpeechTag, LanguageCode languageCode);
}
