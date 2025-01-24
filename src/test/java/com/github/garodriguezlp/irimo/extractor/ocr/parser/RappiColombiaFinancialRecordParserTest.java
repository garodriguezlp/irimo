package com.github.garodriguezlp.irimo.extractor.ocr.parser;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.garodriguezlp.irimo.domain.FinancialRecord;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RappiColombiaFinancialRecordParserTest {

  private RappiColombiaFinancialRecordParser parser;

  @BeforeEach
  void setUp() {
    parser = new RappiColombiaFinancialRecordParser();
  }

  @Test
  void testExtractRecords() {
    // given
    String ocrText = expectedTextOnRappiImage();

    // when
    List<FinancialRecord> records = parser.extractRecords(ocrText);

    // then
    assertThat(records)
        .hasSize(8)
        .containsExactlyInAnyOrder(
            new FinancialRecord(LocalDate.parse("2024-10-09"), "Mercadopago Colombia L", new BigDecimal("-79900"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-20"), "Mercadopago", new BigDecimal("-86000"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-09"), "Pago Con Pse", new BigDecimal("1337971"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-07"), "Rappi - Chevignon, Jardin Pla...", new BigDecimal("-140150"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-07"), "Tienda De Maskotas Esp", new BigDecimal("-781000"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-06"), "Estacion De Servicio", new BigDecimal("-177371"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-05"), "Migatte", new BigDecimal("-41600"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-05"), "Rappi - La Rebaja, Plus No 13 - ...", new BigDecimal("-66050"), "Rappi")
        );
  }

  @Test
  void testExtractRecords2() {
    // given
    String ocrText = expectedTextOnRappiImage2();

    // when
    List<FinancialRecord> records = parser.extractRecords(ocrText);

    // then
    assertThat(records)
        .hasSize(7)
        .containsExactlyInAnyOrder(
            new FinancialRecord(LocalDate.parse("2024-10-30"), "Pago Con Pse", new BigDecimal("2397773.75"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-29"), "Rappi - Burritos & Co - Ingenio", new BigDecimal("-30550"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-29"), "Rappi - Panaderia Paola (valle...", new BigDecimal("-21950"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-27"), "Enterprise Rent A Car", new BigDecimal("-420235.43"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-26"), "The Book Loft", new BigDecimal("-373088.13"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-25"), "Sunoco", new BigDecimal("-1"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-25"), "Alamo Rent A Car Rental", new BigDecimal("-501222.91"), "Rappi")
        );
  }

  private static String expectedTextOnRappiImage() {
    return """
        Mercadopago Colombia L
        $79.900
        9 Oct 2024 11:16
        Pending

        Mercadopago
        $86.000
        20 Oct 2024 11:13
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
        + $660,50
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
        + $305,50

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
