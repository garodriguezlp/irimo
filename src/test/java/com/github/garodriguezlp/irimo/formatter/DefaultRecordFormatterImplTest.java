package com.github.garodriguezlp.irimo.formatter;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.garodriguezlp.irimo.domain.FinancialRecord;
import com.github.garodriguezlp.irimo.domain.FormattedFinancialRecord;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultRecordFormatterImplTest {

  private DefaultRecordFormatterImpl formatter;

  @BeforeEach
  void setUp() {
    formatter = new DefaultRecordFormatterImpl();
  }

  @Test
  void testFormat() {
    // when
    List<FormattedFinancialRecord> formattedRecords = formatter.format(buildInputRecords());

    // then
    assertThat(formattedRecords)
        .hasSize(3)
        .containsExactlyInAnyOrderElementsOf(expectedRecords());
  }

  private static List<FinancialRecord> buildInputRecords() {
    return List.of(
        new FinancialRecord(LocalDate.parse("2024-10-02"),
            "Agregaste dinero a Home Online Services", new BigDecimal("102500.00"), "Nu"),
        new FinancialRecord(LocalDate.parse("2024-09-30"), "Recibiste de Bancolombia",
            new BigDecimal("7459949.00"), "Nu"),
        new FinancialRecord(LocalDate.parse("2024-09-22"),
            "Agregaste dinero a Home Online Services", new BigDecimal("102500.00"), "Nu"));
  }

  private List<FormattedFinancialRecord> expectedRecords() {
    return List.of(
        new FormattedFinancialRecord("10/02/2024", "Agregaste dinero a Home Online Services",
            "$102,500.00", "Nu"),
        new FormattedFinancialRecord("09/30/2024", "Recibiste de Bancolombia", "$7,459,949.00",
            "Nu"),
        new FormattedFinancialRecord("09/22/2024", "Agregaste dinero a Home Online Services",
            "$102,500.00", "Nu")
    );
  }
}
