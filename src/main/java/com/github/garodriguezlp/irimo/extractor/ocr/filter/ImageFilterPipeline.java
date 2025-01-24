package com.github.garodriguezlp.irimo.extractor.ocr.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.List;

public class ImageFilterPipeline {

  private static final Logger LOGGER = LoggerFactory.getLogger(ImageFilterPipeline.class);

  private final List<ImageFilter> filters;

  public ImageFilterPipeline(List<ImageFilter> filters) {
    this.filters = filters;
  }

  public BufferedImage execute(BufferedImage image) {
    LOGGER.debug("Starting image filtering pipeline with {} filters", filters.size());
    BufferedImage processedImage = image;
    for (ImageFilter filter : filters) {
      LOGGER.info("Applying image filter: {}", filter.getClass().getSimpleName());
      processedImage = filter.apply(processedImage);
    }
    LOGGER.debug("Image filtering pipeline completed");
    return processedImage;
  }
}
