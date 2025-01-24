package com.github.garodriguezlp.irimo.extractor.ocr;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.garodriguezlp.irimo.domain.FinancialRecord;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

@SpringBootTest
class NuColombiaOcrFinancialRecordExtractorTest {

  private static final String INPUT_IMAGE = "nu_colombia.jpeg";

  @Autowired
  private NuColombiaOcrFinancialRecordExtractor extractor;

  @Autowired
  private ResourceLoader resourceLoader;

  @Test
  void testExtractFinancialRecords() throws IOException {
    // given
    File image = getInputImageFileFromResources(INPUT_IMAGE);

    // when
    List<FinancialRecord> records = extractor.extract(List.of(image));

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

  private File getInputImageFileFromResources(String inputImage) throws IOException {
    return resourceLoader.getResource("classpath:" + inputImage).getFile();
  }

}
