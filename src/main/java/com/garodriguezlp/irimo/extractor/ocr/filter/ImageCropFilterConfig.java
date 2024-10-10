package com.garodriguezlp.irimo.extractor.ocr.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageCropFilterConfig {

  @Bean
  public ImageFilter nuColombiaImageCropFilter() {
    return new ImageCropFilter(0.18, 0.2); // Example values, adjust as needed
  }


}
