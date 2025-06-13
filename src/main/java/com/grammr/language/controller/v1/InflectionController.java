package com.grammr.language.controller.v1;

import com.grammr.domain.value.language.ParadigmDTO;
import com.grammr.language.controller.v1.dto.InflectionsRequest;
import com.grammr.language.service.v1.InflectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Inflection", description = "Inflection API for generating word paradigms")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class InflectionController {

  private final InflectionService inflectionService;

  @Operation(summary = "Inflect a word", description = """
      Retrieve inflected forms of a word. Stores all inflections in the database.
      Unique paradigm identifier is returned in the API response as paradigmId.
      """)
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Word inflected successfully"),
      @ApiResponse(responseCode = "422", description = "Language or part of speech not supported",
          content = @Content(
              mediaType = "application/json",
              examples = @ExampleObject(
                  name = "UnsupportedLanguageOrPOS",
                  summary = "Unsupported language or part of speech",
                  value = "{\n  \"message\": \"Language or part of speech not supported\"\n}"
              )
          )),
  })
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Inflections request example",
      required = true,
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
              name = "InflectionsRequestExample",
              summary = "Example inflection request",
              value = "{\n  \"word\": \"laufen\",\n  \"languageCode\": \"de\",\n  \"pos\": \"VERB\"\n}"
          )
      )
  )
  @PostMapping(value = "/inflection", produces = "application/json")
  public ResponseEntity<ParadigmDTO> performInflection(@RequestBody @Valid InflectionsRequest request) {
    log.info("Processing inflection request {}", request);
    var paradigm = inflectionService.inflect(request);
    return ResponseEntity.ok(paradigm);
  }
}
