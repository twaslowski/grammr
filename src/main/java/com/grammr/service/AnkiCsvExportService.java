package com.grammr.service;

import com.grammr.domain.entity.Flashcard;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnkiCsvExportService {

  private static final String DELIMITER = ",";
  private static final String NEWLINE = "\n";

  public byte[] exportDeck(List<Flashcard> flashcards) {
    var csv = new StringBuilder();
    for (var flashcard : flashcards) {
      csv = appendFlashcard(csv, flashcard);
    }
    return zipCsv(csv.toString());
  }

  private StringBuilder appendFlashcard(StringBuilder csv, Flashcard flashcard) {
    return csv.append(flashcard.getQuestion()).append(DELIMITER)
        .append(flashcard.getAnswer()).append(NEWLINE);
  }

  @SneakyThrows
  private byte[] zipCsv(String csvContent) {
    try (var byteArrayOutputStream = new ByteArrayOutputStream();
        var zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
      var zipEntry = new ZipEntry("deck.csv");
      zipOutputStream.putNextEntry(zipEntry);
      zipOutputStream.write(csvContent.getBytes());
      zipOutputStream.closeEntry();
      zipOutputStream.finish();
      return byteArrayOutputStream.toByteArray();
    }
  }
}
