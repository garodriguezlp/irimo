package com.github.garodriguezlp.irimo.extractor.ocr.parser;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.garodriguezlp.irimo.domain.FinancialRecord;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NuColombiaFinancialRecordParserTest {

  private NuColombiaFinancialRecordParser parser;

  @BeforeEach
  void setUp() {
    parser = new NuColombiaFinancialRecordParser();
  }

  @Test
  void testExtractRecords() {
    // given
    String ocrText = """
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

    // when
    List<FinancialRecord> records = parser.extractRecords(ocrText);

    // then
    assertThat(records)
        .hasSize(6)
        .containsExactlyInAnyOrder(
            new FinancialRecord(LocalDate.parse("2024-10-02"), "Agregaste dinero a Home Online Services", new BigDecimal("102500.00"), "Nu"),
            new FinancialRecord(LocalDate.parse("2024-09-30"), "Recibiste de Bancolombia", new BigDecimal("7459949.00"), "Nu"),
            new FinancialRecord(LocalDate.parse("2024-09-22"), "Agregaste dinero a Home Online Services", new BigDecimal("102500.00"), "Nu"),
            new FinancialRecord(LocalDate.parse("2024-09-14"), "Agregaste dinero a foo", new BigDecimal("1000000.00"), "Nu"),
            new FinancialRecord(LocalDate.parse("2024-09-14"), "Agregaste dinero a Mi primera Cajita", new BigDecimal("1000000.00"), "Nu"),
            new FinancialRecord(LocalDate.parse("2024-09-13"), "Recibiste de Bancolombia", new BigDecimal("5115298.00"), "Nu"));
  }
}
