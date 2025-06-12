package com.grammr.language.service.v2.translation;

public class Prompts {

  private Prompts() {}

  public static final String SYSTEM_PROMPT = """
      You are a translation engine. Translate user input from Language A to Language B.
      Only return the translated sentence. Do not include any explanations, comments, or formatting.
      """;

  public static final String USER_PROMPT = """
       Translate the following from %s to %s:\\n\\"%s\\"
      """;
}
