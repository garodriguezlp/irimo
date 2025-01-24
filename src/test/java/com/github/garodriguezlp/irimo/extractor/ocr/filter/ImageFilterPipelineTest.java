package com.github.garodriguezlp.irimo.extractor.ocr.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ImageFilterPipelineTest {

  @Mock
  private ImageFilter firstFilter;

  @Mock
  private ImageFilter secondFilter;

  private ImageFilterPipeline pipeline;

  @BeforeEach
  void setUp() {
    pipeline = new ImageFilterPipeline(List.of(firstFilter, secondFilter));
  }

  @Test
  void execute_appliesAllFiltersInOrder() {
    // given
    BufferedImage image = createTestImage();
    BufferedImage intermediateImage = createTestImage();
    BufferedImage finalImage = createTestImage();
    when(firstFilter.apply(image)).thenReturn(intermediateImage);
    when(secondFilter.apply(intermediateImage)).thenReturn(finalImage);

    // when
    BufferedImage resultImage = pipeline.execute(image);

    // then
    assertThat(resultImage)
        .isEqualTo(finalImage);

    InOrder inOrder = inOrder(firstFilter, secondFilter);
    inOrder.verify(firstFilter).apply(image);
    inOrder.verify(secondFilter).apply(intermediateImage);
  }

  private BufferedImage createTestImage() {
    BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    for (int i = 0; i < 10; i++) {
      int x = (int) (Math.random() * 100);
      int y = (int) (Math.random() * 100);
      int rgb = new Color((int) (Math.random() * 255), (int) (Math.random() * 255),
          (int) (Math.random() * 255)).getRGB();
      img.setRGB(x, y, rgb);
    }
    return img;
  }
}
