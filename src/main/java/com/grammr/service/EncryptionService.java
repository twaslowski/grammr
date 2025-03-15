package com.grammr.service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EncryptionService {

  private final TextEncryptor encryptor;
  private final Mac mac;

  public EncryptionService(
      @Value("${spring.security.encryption.key}") String encryptionKey,
      @Value("${spring.security.encryption.salt}") String salt) {

    if (encryptionKey == null || encryptionKey.isBlank()) {
      throw new IllegalArgumentException("Encryption key is required");
    }
    if (salt == null || salt.isBlank()) {
      throw new IllegalArgumentException("Salt is required");
    }

    try {
      mac = Mac.getInstance("HmacSHA256");
      SecretKeySpec keySpec = new SecretKeySpec(
          encryptionKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
      mac.init(keySpec);
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      throw new IllegalStateException("Failed to initialize HMAC", e);
    }

    this.encryptor = Encryptors.text(encryptionKey, salt);
  }

  public String encryptEmail(String email) {
    if (email == null) {
      return null;
    }
    return encryptor.encrypt(email);
  }

  public String decryptEmail(String encryptedEmail) {
    if (encryptedEmail == null) {
      return null;
    }
    return encryptor.decrypt(encryptedEmail);
  }

  /**
   * Computes a deterministic keyed hash of the email for lookup purposes.
   * This hash can be stored in a separate column.
   */
  public String computeEmailHash(String email) {
    if (email == null) {
      return null;
    }
    byte[] hashBytes = mac.doFinal(email.getBytes(StandardCharsets.UTF_8));
    return new String(Hex.encode(hashBytes));
  }
}

