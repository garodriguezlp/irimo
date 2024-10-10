package com.garodriguezlp.irimo.extractor.ocr.filter;

import static com.garodriguezlp.irimo.util.ImageUtils.loadImageFromClasspath;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class ImageCropFilterTest {

  @Test
  void testApplyShouldCropImageCorrectly() throws IOException {
    // given
    BufferedImage inputImage = loadImageFromClasspath("/nu_colombia.jpeg");
    BufferedImage expectedCroppedImage = loadImageFromClasspath("/nu_colombia_cropped.jpeg");

    // when
    BufferedImage croppedImage = new ImageCropFilter(0.18, 0.22).apply(inputImage);

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
