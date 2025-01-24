package com.github.garodriguezlp.irimo.extractor.ocr.processor;

import static com.github.garodriguezlp.irimo.util.ImageUtils.loadImageFromClasspath;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AWSTextractOcrProcessorTest {

  @Test
  void testOcrExtractionFromNuColombiaImage() throws IOException {
    // given
    BufferedImage image = loadImageFromClasspath("/nu_colombia_cropped.jpeg");

    // when
    String result = new AWSTextractOcrProcessor(0.058f).performOcr(image);

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
    String result = new AWSTextractOcrProcessor(0.088f).performOcr(image);

    // then
    assertThat(result)
        .isNotBlank()
        .contains(expectedTextOnRappiImage());
  }

  @Test
  void testOcrExtractionFromRappiColombiaImage2() throws IOException {
    // given
    BufferedImage image = loadImageFromClasspath("/rappi_colombia_2_cropped.jpeg");

    // when
    String result = new AWSTextractOcrProcessor(0.088f).performOcr(image);

    // then
    assertThat(result)
        .isNotBlank()
        .contains(expectedTextOnRappiImage2());
  }

  private static String expectedTextOnNuImage() {
    return """
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
        """;
  }

  private static String expectedTextOnRappiImage2() {
    return """
        Pago Con Pse
        -$2.397.773,75
        30 Oct 2024 08:25

        Rappi - Burritos & Co - Ingenio
        $30.550
        29 Oct 2024 19:08

        Rappi - Panaderia Paola (valle...
        $21.950
        29 Oct 2024 08:04
        + $219,50

        Enterprise Rent A Car
        $420.235,43
        27 Oct 2024 11:45
        Pending

        The Book Loft
        $373.088,13
        26 Oct 2024 16:55
        + $3.730,88
        24 installments

        Sunoco
        $1
        25 Oct 2024 12:02
        Rejected

        Alamo Rent A Car Rental
        $501.222,91
        25 Oct 2024 09:50
        + $5.012,23
        24 installments

        Rappi - Prime
        24 Oct 2024 11:01
        """;
  }
}
