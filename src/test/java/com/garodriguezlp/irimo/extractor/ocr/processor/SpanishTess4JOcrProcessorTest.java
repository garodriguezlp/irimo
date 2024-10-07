package com.garodriguezlp.irimo.extractor.ocr.processor;

import static com.garodriguezlp.irimo.util.ImageUtils.loadImageFromClasspath;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpanishTess4JOcrProcessorTest {

  private SpanishTess4JOcrProcessor processor;

  @BeforeEach
  void setUp() {
    processor = new SpanishTess4JOcrProcessor();
  }

  @Test
  void testPerformOcr() throws IOException {
    // given
    BufferedImage image = loadImageFromClasspath("/nu_colombia_cropped.jpeg");

    // when
    String result = processor.performOcr(image);

    // then
    assertThat(result)
        .isNotBlank()
        .contains("""
            Buscar movimiento
            Agregaste dinero a +$102.500,00
            Home Online Services
            02 oct - 22:12
            Recibiste de +$7.459.949,00
            Bancolombia
            30 sep - 09:44
            Agregaste dinero a +$102.500,00
            Home Online Services
            22 sep - 09:29
            Agregaste dineroa + +$1.000.000,00
            foo
            14 sep - 18:35
            Agregaste dineroa + +$1.000.000,00
            Mi primera Cajita
            14 sep - 18:35
            Recibiste de +$5.115.298,00
            Bancolombia
            13 sep - 17:19
            Agregaste dineroa  +$1.000.000,00
            """);
  }

}
