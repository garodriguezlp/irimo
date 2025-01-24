package com.github.garodriguezlp.irimo.extractor.ocr.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageCropFilterConfig {

  @Bean
  public ImageFilter nuColombiaImageCropFilter() {
    return new ImageCropFilter(0.18, 0.20);
  }

  @Bean
  public ImageFilter rappiColombiaImageCropFilter() {
    return new ImageCropFilter(0.19, 0.19);
  }

}
