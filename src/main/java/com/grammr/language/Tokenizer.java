package com.grammr.language;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class Tokenizer {

  public Collection<String> tokenize(String phrase) {
    return Arrays.stream(phrase.split("\\P{L}+"))
        .filter(token -> !token.isEmpty())
        .collect(Collectors.toList());
  }
}
