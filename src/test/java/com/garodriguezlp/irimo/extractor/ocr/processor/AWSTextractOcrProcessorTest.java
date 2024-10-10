package com.garodriguezlp.irimo.extractor.ocr.processor;

import static com.garodriguezlp.irimo.util.ImageUtils.loadImageFromClasspath;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AWSTextractOcrProcessorTest {

  private AWSTextractOcrProcessor processor;

  @BeforeEach
  void setUp() {
    processor = new AWSTextractOcrProcessor();
  }

  @Test
  void testOcrExtractionFromNuColombiaImage() throws IOException {
    // given
    BufferedImage image = loadImageFromClasspath("/nu_colombia.jpeg");

    // when
    String result = processor.performOcr(image);

    // then
    assertThat(result)
        .isNotBlank()
        .contains(expectedTextOnNuImage());
  }

  @Test
  void testOcrExtractionFromRappiColombiaImage() throws IOException {
    // given
    BufferedImage image = loadImageFromClasspath("/rappi_colombia_cropped.jpeg");

    // when
    String result = processor.performOcr(image);

    // then
    assertThat(result)
        .isNotBlank()
        .contains(expectedTextOnRappiImage());
  }

  private static String expectedTextOnNuImage() {
    return """
        ?
        Q
        Buscar movimiento
        Agregaste dinero a
        +$102.500,00
        Home Online Services
        02 oct - 22:12
        Recibiste de
        +$7.459.949,00
        Bancolombia
        30 sep - 09:44
        Agregaste dinero a
        +$102.500,00
        Home Online Services
        22 sep - 09:29
        Agregaste dinero a
        +$1.000.000,00
        foo
        14 sep - 18:35
        Agregaste dinero a
        +$1.000.000,00
        Mi primera Cajita
        14 sep - 18:35
        Recibiste de
        +$5.115.298,00
        Bancolombia
        13 sep - 17:19
        Agregaste dinero a
        +$1.000.000,00
        """;
  }

  private static String expectedTextOnRappiImage() {
    return """
        Mercadopago Colombia L
        $79.900
        9 Oct 2024 11:16
        Pending
        Mercadopago
        $86.000
        9 Oct 2024 11:13
        Pending
        Pago Con Pse
        e
        -$1.337.971
        9 Oct 2024 09:59
        Rappi - Chevignon, Jardin Pla...
        $140.150
        7 Oct 2024 15:42
        + $1.401,50
        Tienda De Maskotas Esp
        $781.000
        7 Oct 2024 09:14
        Estacion De Servicio
        $177.371
        6 Oct 2024 15:13
        Migatte
        $41.600
        5 Oct 2024 18:13
        Rappi - La Rebaja, Plus No 13 - ... $66.050
        5 Oct 2024 19:52
        + $660,50
        """;
  }
}
