package com.grammr.language.service.v2.translation.word;

public class Prompts {

  private Prompts() {}

  public static final String SYSTEM_PROMPT = """
      You are a translation engine. Your task is to translate a specific word within the context of a provided sentence.
      Only return the translation of that word. Do not include any explanations, comments, or formatting.
      """;

  public static final String USER_PROMPT = """
       Translate the word "%s" to %s in the context of the following sentence: "%s".
       Ensure that the translation is accurate and fits naturally within the context.
      """;
}
