package com.github.garodriguezlp.irimo.extractor.ocr.processor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSTextractOcrProcessorConfig {

  @Bean
  public AWSTextractOcrProcessor rappiAWSTextractOcrProcessor() {
    return new AWSTextractOcrProcessor(0.078f);
  }

  @Bean
  public AWSTextractOcrProcessor nuAWSTextractOcrProcessor() {
    return new AWSTextractOcrProcessor(0.058f);
  }

}
