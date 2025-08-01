package com.grammr.language.service.v2.translation.word;

public class Prompts {

  private Prompts() {}

  public static final String SYSTEM_PROMPT = """
      You are a translation engine. Your task is to translate a specific word within the context of a provided sentence.
      Only return the translation of that word. Do not include any explanations, comments, or formatting.
      """;

  public static final String USER_PROMPT = """
       Translate the word "%s" to %s in the context of the following sentence: "%s".
       Ensure that the translation is close to the original meaning and fits naturally within the context.
       When in doubt between contextual and literal translation, prefer the literal translation.
      """;

  public static final String NO_CONTEXT_SYSTEM_PROMPT = """
      You are a translation engine. Your task is to translate single words into other languages.
      Only return the translation of that word. Do not include any explanations, comments, or formatting.
      """;

  public static final String NO_CONTEXT_USER_PROMPT = """
       Translate the word "%s" to %s. Ensure that the translation is accurate.
       If the word has multiple meanings, choose the most common one.
      """;
}
