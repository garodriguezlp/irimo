package com.github.garodriguezlp.irimo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.github.garodriguezlp.irimo.exporter.clipboard.SystemClipboardService;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;

@SpringBootTest
class FinancialRecordsConsolidationServiceTest {

  private static final String INPUT_IMAGE = "nu_colombia.jpeg";

  @Autowired
  private FinancialRecordsConsolidationService service;

  @Autowired
  private ResourceLoader resourceLoader;

  @MockBean
  private SystemClipboardService clipboardService;

  @Captor
  private ArgumentCaptor<String> clipboardContentCaptor;

  @Test
  void testProcessRecordsForNuTargetingClipboard() throws IOException {
    // given
    File image = resourceLoader.getResource("classpath:" + INPUT_IMAGE).getFile();

    // when
    service.processRecords(List.of(image), "nu", "clipboard");

    // then
    verify(clipboardService).copy(clipboardContentCaptor.capture());
    String clipboardContent = clipboardContentCaptor.getValue();
    assertThat(clipboardContent)
        .isNotEmpty()
        .isNotEqualTo(expectedOutput());
  }

  private static String expectedOutput() {
    return """
        09/13/2024	Recibiste de Bancolombia	$5,115,298.00	Nu
        09/14/2024	Agregaste dineroa foo	$1,000,000.00	Nu
        09/14/2024	Agregaste dineroa Mi primera Cajita	$1,000,000.00	Nu
        09/22/2024	Agregaste dinero a Home Online Services	$102,500.00	Nu
        09/30/2024	Recibiste de Bancolombia	$7,459,949.00	Nu
        10/02/2024	Agregaste dinero a Home Online Services	$102,500.00	Nu
        """;
  }

}
