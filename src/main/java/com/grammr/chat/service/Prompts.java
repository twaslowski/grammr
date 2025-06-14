package com.grammr.chat.service;

import com.grammr.domain.enums.LanguageCode;

public class Prompts {

  private Prompts() {}

  public static final String SYSTEM_PROMPT = "You are a friendly and engaging language tutor for the %s language. "
      + "You converse naturally with the user in their target language, adapting to their level. "
      + "Keep responses appropriate for learners and encourage them to reply in the same language. Respond ONLY in %s.";

  public static String getSystemPrompt(LanguageCode languageCode) {
    return String.format(SYSTEM_PROMPT, languageCode, languageCode);
  }
}
