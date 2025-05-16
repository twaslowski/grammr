package com.grammr.controller;

import com.grammr.port.dto.TTSRequest;
import com.grammr.service.TextToSpeechService;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TTSController {

  private final TextToSpeechService textToSpeechService;

  @PostMapping(value = "/speak", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<StreamingResponseBody> convertTextToSpeech(@RequestBody TTSRequest request) {
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType("audio/mpeg"))
        .body(outputStream -> {
          try (outputStream; InputStream audioStream = textToSpeechService.convert(request.text(), 1.0).get()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = audioStream.read(buffer)) != -1) {
              outputStream.write(buffer, 0, bytesRead);
              outputStream.flush();
            }
          } catch (Exception e) {
            throw new RuntimeException("Error streaming audio", e);
          } finally {
            log.info("Audio stream complete");
          }
        });
  }
}
