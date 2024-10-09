package com.garodriguezlp.irimo.extractor.ocr.filter;

import com.garodriguezlp.irimo.extractor.ocr.exception.ImageFilteringException;
import java.awt.image.BufferedImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NuColombiaImageCropFilter implements ImageFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(NuColombiaImageCropFilter.class);
  private static final double CROP_PERCENTAGE = 0.18;

  @Override
  public BufferedImage apply(BufferedImage image) {
    try {
      LOGGER.debug("Applying NuColombiaImageCropFilter");
      BufferedImage imageCopy = copyImage(image);
      ImageDimensions dimensions = calculateDimensions(imageCopy);
      return crop(imageCopy, dimensions);
    } catch (Exception e) {
      throw new ImageFilteringException("Failed to apply NuColombiaImageCropFilter", e);
    }
  }

  private BufferedImage copyImage(BufferedImage image) {
    BufferedImage copyImage = new BufferedImage(image.getWidth(), image.getHeight(),
        image.getType());
    copyImage.getGraphics().drawImage(image, 0, 0, null);
    return copyImage;
  }

  private ImageDimensions calculateDimensions(BufferedImage image) {
    int originalWidth = image.getWidth();
    int originalHeight = image.getHeight();
    int cropWidth = (int) (originalWidth * CROP_PERCENTAGE);
    int newWidth = originalWidth - cropWidth;

    ImageDimensions dimensions = new ImageDimensions(originalWidth, originalHeight, cropWidth,
        newWidth);

    if (dimensions.newWidth() <= 0) {
      LOGGER.error("Crop percentage is too large. The resulting image would have no width.");
      throw new IllegalArgumentException(
          "Crop percentage is too large. The resulting image would have no width.");
    }

    return dimensions;
  }

  private BufferedImage crop(BufferedImage copyImage, ImageDimensions dimensions) {
    LOGGER.debug("Cropping image from left to right");
    BufferedImage result = copyImage.getSubimage(
        dimensions.cropWidth(),
        0,
        dimensions.newWidth(),
        dimensions.originalHeight());
    copyImage.getGraphics().dispose();
    return result;
  }

  private record ImageDimensions(
      int originalWidth,
      int originalHeight,
      int cropWidth,
      int newWidth
  ) {

  }
}
