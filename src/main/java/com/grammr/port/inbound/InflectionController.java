package com.grammr.port.inbound;

import com.grammr.domain.enums.PartOfSpeechTag;
import com.grammr.domain.exception.InflectionNotAvailable;
import com.grammr.domain.value.language.Inflections;
import com.grammr.port.dto.InflectionsRequest;
import com.grammr.service.InflectionService;
import io.micrometer.core.annotation.Timed;
import java.util.Set;
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
  public ResponseEntity<Inflections> performInflection(@RequestBody InflectionsRequest request) {
    log.info("Processing inflection request {}", request);
    var inflections = inflectionService.inflect(request.languageCode(), request.token());
    return ResponseEntity.ok(inflections);
  }
}
