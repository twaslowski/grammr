package com.grammr.port.rest;

import static org.assertj.core.api.Assertions.assertThat;

import com.grammr.annotation.IntegrationTest;
import com.grammr.domain.value.language.Token;
import com.grammr.domain.value.language.TokenMorphologySpec;
import com.grammr.integration.IntegrationTestBase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

@IntegrationTest
class InflectionGeneratorPortTest extends IntegrationTestBase {

  @Test
  @SneakyThrows
  void shouldReturnValidJson() {
    var word = "дела";
    Token token = Token.builder()
        .morphology(TokenMorphologySpec.valid()
            .lemma(word)
            .build())
        .build();
    var result = inflectionGeneratorPort.generateInflections(token);
    var parsed = objectMapper.readTree(result);
    assertThat(parsed.get("success").booleanValue()).isTrue();
    var data = parsed.get("data");
    assertThat(data.get("gent")).isNotNull();
  }
}