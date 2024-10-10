package com.garodriguezlp.irimo.extractor.ocr.parser;

import static org.assertj.core.api.Assertions.assertThat;

import com.garodriguezlp.irimo.domain.FinancialRecord;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RappiColombiaFinancialRecordParserTest {

  private RappiColombiaFinancialRecordParser parser ;

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

    records.forEach(record -> System.out.println(record));

    // then
    assertThat(records)
        .hasSize(8)
        .containsExactlyInAnyOrder(
            new FinancialRecord(LocalDate.parse("2024-10-09"), "Mercadopago Colombia L", new BigDecimal("79900"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-20"), "Mercadopago", new BigDecimal("86000"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-09"), "Pago Con Pse e", new BigDecimal("-1337971"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-07"), "Rappi - Chevignon, Jardin Pla...", new BigDecimal("140150"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-07"), "Tienda De Maskotas Esp", new BigDecimal("781000"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-06"), "Estacion De Servicio", new BigDecimal("177371"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-05"), "Migatte", new BigDecimal("41600"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-05"), "Rappi - La Rebaja, Plus No 13 - ...", new BigDecimal("66050"), "Rappi")
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
