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
class RappiColombiaOcrFinancialRecordExtractorTest {

  private static final String INPUT_IMAGE = "rappi_colombia.jpeg";

  @Autowired
  private RappiColombiaOcrFinancialRecordExtractor extractor;

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
        .hasSize(8)
        .containsExactlyInAnyOrder(
            new FinancialRecord(LocalDate.parse("2024-10-09"), "Mercadopago Colombia L", new BigDecimal("-79900"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-09"), "Mercadopago", new BigDecimal("-86000"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-09"), "Pago Con Pse", new BigDecimal("1337971"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-07"), "Rappi - Chevignon, Jardin Pla...", new BigDecimal("-140150"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-07"), "Tienda De Maskotas Esp", new BigDecimal("-781000"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-06"), "Estacion De Servicio", new BigDecimal("-177371"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-05"), "Migatte", new BigDecimal("-41600"), "Rappi"),
            new FinancialRecord(LocalDate.parse("2024-10-05"), "Rappi - La Rebaja, Plus No 13 - ...", new BigDecimal("-66050"), "Rappi")
        );
  }

  private File getInputImageFileFromResources(String inputImage) throws IOException {
    return resourceLoader.getResource("classpath:" + inputImage).getFile();
  }

}
