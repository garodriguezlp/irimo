package com.github.garodriguezlp.irimo.extractor.ocr.filter;

import static com.github.garodriguezlp.irimo.util.ImageUtils.loadImageFromClasspath;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;

class ImageCropFilterTest {

  @Test
  void testApplyShouldCropImageCorrectly() throws IOException {
    // given
    BufferedImage inputImage = loadImageFromClasspath("/rappi_colombia.jpeg");
    BufferedImage expectedCroppedImage = loadImageFromClasspath("/rappi_colombia_cropped.jpeg");

    // when
    BufferedImage croppedImage = new ImageCropFilter(0.19, 0.19).apply(inputImage);

    // then
    // @todo: Improve image assertion beyond just comparing the dimensions
    assertThat(croppedImage)
        .isNotNull()
        .satisfies(image -> {
          assertThat(image.getWidth()).isEqualTo(expectedCroppedImage.getWidth());
          assertThat(image.getHeight()).isEqualTo(expectedCroppedImage.getHeight());
        });
  }
}
