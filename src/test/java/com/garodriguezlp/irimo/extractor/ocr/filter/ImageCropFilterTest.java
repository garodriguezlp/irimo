package com.garodriguezlp.irimo.extractor.ocr.filter;

import static com.garodriguezlp.irimo.util.ImageUtils.loadImageFromClasspath;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImageCropFilterTest {

  private ImageCropFilter filter;

  @BeforeEach
  void setUp() {
    filter = new ImageCropFilter(0.18, 0);
  }

  @Test
  void testApplyShouldCropImageCorrectly() throws IOException {
    // given
    BufferedImage inputImage = loadImageFromClasspath("/nu_colombia_input.jpeg");
    BufferedImage expectedCroppedImage = loadImageFromClasspath("/nu_colombia_cropped.jpeg");

    // when
    BufferedImage croppedImage = filter.apply(inputImage);

    // then
    // @todo: Improve image assertion beyond just comparing the dimensions
    assertThat(croppedImage)
        .isNotNull()
        .satisfies(image -> {
          assertThat(image.getWidth()).isEqualTo(expectedCroppedImage.getWidth());
          assertThat(image.getHeight()).isEqualTo(expectedCroppedImage.getHeight());
        });
  }

  // @todo: delete me
  @Test
  void poc() throws IOException {
    // given
    BufferedImage inputImage = loadImageFromClasspath("/rappi_colombia.jpeg");

    // when
    BufferedImage croppedImage = filter.apply(inputImage);

    // then
    File output = new File("/Users/gustavo.rodriguezl/src/garodriguezlp/irimo/rappi_colombia_cropped.jpeg");
    ImageIO.write(croppedImage, "JPEG", output);
  }
}
