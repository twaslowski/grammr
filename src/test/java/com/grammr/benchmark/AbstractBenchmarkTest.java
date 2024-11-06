package com.grammr.benchmark;

import com.grammr.annotation.BenchmarkTest;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@BenchmarkTest
public class AbstractBenchmarkTest {

  // metrics to collect:
  // time
  // amount of consumed/generated tokens -> cost
}
