package com.grammr.language.controller.v1;

import com.grammr.domain.value.language.ParadigmDTO;
import com.grammr.language.controller.v1.dto.InflectionsRequest;
import com.grammr.language.service.v1.InflectionService;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class InflectionController {

  private final InflectionService inflectionService;

  @Timed(value = "rest.inflection", description = "Time taken to inflect words")
  @PostMapping(value = "/inflection", produces = "application/json")
  public ResponseEntity<ParadigmDTO> performInflection(@RequestBody @Valid InflectionsRequest request) {
    log.info("Processing inflection request {}", request);
    var paradigm = inflectionService.inflect(request);
    return ResponseEntity.ok(paradigm);
  }
}
