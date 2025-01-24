package com.github.garodriguezlp.irimo.extractor.ocr.filter;

import com.github.garodriguezlp.irimo.extractor.ocr.exception.ImageFilteringException;
import java.awt.image.BufferedImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageCropFilter implements ImageFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(ImageCropFilter.class);
  private final double verticalCropPercentage;
  private final double horizontalCropPercentage;

  public ImageCropFilter(
      double verticalCropPercentage,
      double horizontalCropPercentage) {
    this.verticalCropPercentage = verticalCropPercentage;
    this.horizontalCropPercentage = horizontalCropPercentage;
  }

  /**
   * Crops the given image starting from the top-left corner based on the specified vertical and
   * horizontal crop percentages. The method removes a portion of the image from the top and left
   * sides according to the provided percentages.
   *
   * @param image the original image to be cropped
   * @return a new BufferedImage that represents the cropped area of the original image
   * @throws ImageFilteringException if the cropping operation fails
   */
  @Override
  public BufferedImage apply(BufferedImage image) {
    try {
      LOGGER.debug(
          "Applying ImageCropFilter with vertical crop percentage: {} and horizontal crop percentage: {}",
          verticalCropPercentage, horizontalCropPercentage);
      BufferedImage imageCopy = copyImage(image);
      ImageDimensions dimensions = calculateDimensions(imageCopy);
      return crop(imageCopy, dimensions);
    } catch (Exception e) {
      throw new ImageFilteringException("Failed to apply ImageCropFilter", e);
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
    int cropWidth = (int) (originalWidth * verticalCropPercentage);
    int cropHeight = (int) (originalHeight * horizontalCropPercentage);
    int newWidth = originalWidth - cropWidth;
    int newHeight = originalHeight - cropHeight;

    ImageDimensions dimensions = new ImageDimensions(cropWidth, newWidth, cropHeight, newHeight);

    if (dimensions.newWidth() <= 0 || dimensions.newHeight() <= 0) {
      LOGGER.error(
          "Crop percentage is too large. The resulting image would have no width or height.");
      throw new IllegalArgumentException(
          "Crop percentage is too large. The resulting image would have no width or height.");
    }

    return dimensions;
  }

  private BufferedImage crop(BufferedImage copyImage, ImageDimensions dimensions) {
    LOGGER.debug("Cropping image");
    BufferedImage result = copyImage.getSubimage(
        dimensions.cropWidth(),
        dimensions.cropHeight(),
        dimensions.newWidth(),
        dimensions.newHeight());
    copyImage.getGraphics().dispose();
    return result;
  }

  private record ImageDimensions(
      int cropWidth,
      int newWidth,
      int cropHeight,
      int newHeight
  ) {

  }
}
