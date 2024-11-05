package com.grammr.benchmark;

import com.grammr.annotation.BenchmarkTest;
import com.grammr.language.recognition.LanguageRecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@BenchmarkTest
public class AbstractBenchmarkTest {

  @Autowired
  protected LanguageRecognitionService languageRecognitionService;
}
