package com.grammr.repository.converter;

import com.grammr.service.EncryptionService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter
@RequiredArgsConstructor
public class EmailEncryptionConverter implements AttributeConverter<String, String> {

  private final EncryptionService encryptionService;

  @Override
  public String convertToDatabaseColumn(String email) {
    return encryptionService.encryptEmail(email);
  }

  @Override
  public String convertToEntityAttribute(String encryptedEmail) {
    return encryptionService.decryptEmail(encryptedEmail);
  }
}
