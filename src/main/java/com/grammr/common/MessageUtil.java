package com.grammr.common;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageUtil {

  private final MessageSource messageSource;

  public String parameterizeMessage(String code, Object... args) {
    return messageSource.getMessage(code, args, Locale.ENGLISH);
  }
}
