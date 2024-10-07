package com.garodriguezlp.irimo.extractor.ocr.it;

import static org.assertj.core.api.Assertions.assertThat;

import com.garodriguezlp.irimo.domain.FinancialRecord;
import com.garodriguezlp.irimo.extractor.ocr.OcrFinancialRecordExtractor;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

@SpringBootTest
class NuColombiaOcrFinancialRecordExtractorITTest {

  private static final String INPUT_IMAGE = "nu_colombia_input.jpeg";
  @Autowired
  private OcrFinancialRecordExtractor nuColombiaOcrFinancialRecordExtractor;

  @Test
  void testExtractFinancialRecords(@TempDir Path tempDir) throws IOException {
    // given
    prepareInputDir(tempDir);

    // when
    List<FinancialRecord> records = nuColombiaOcrFinancialRecordExtractor.extract(tempDir.toFile());

    // then
    assertThat(records)
        .hasSize(6)
        .containsExactlyInAnyOrder(
            new FinancialRecord(LocalDate.parse("2024-10-02"), "Agregaste dinero a Home Online Services", new BigDecimal("102500.00"), "Nu Colombia"),
            new FinancialRecord(LocalDate.parse("2024-09-30"), "Recibiste de Bancolombia", new BigDecimal("7459949.00"), "Nu Colombia"),
            new FinancialRecord(LocalDate.parse("2024-09-22"), "Agregaste dinero a Home Online Services", new BigDecimal("102500.00"), "Nu Colombia"),
            new FinancialRecord(LocalDate.parse("2024-09-14"), "Agregaste dineroa foo", new BigDecimal("1000000.00"), "Nu Colombia"),
            new FinancialRecord(LocalDate.parse("2024-09-14"), "Agregaste dineroa Mi primera Cajita", new BigDecimal("1000000.00"), "Nu Colombia"),
            new FinancialRecord(LocalDate.parse("2024-09-13"), "Recibiste de Bancolombia", new BigDecimal("5115298.00"), "Nu Colombia"));
  }

  private static void prepareInputDir(Path tempDir) throws IOException {
    Files.copy(
        new ClassPathResource("/" + INPUT_IMAGE).getInputStream(),
        tempDir.resolve(INPUT_IMAGE)
    );
  }
}
