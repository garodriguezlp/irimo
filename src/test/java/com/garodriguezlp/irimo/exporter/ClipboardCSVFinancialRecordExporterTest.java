package com.garodriguezlp.irimo.exporter;

import com.garodriguezlp.irimo.domain.FinancialFormattedRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ClipboardCSVFinancialRecordExporterTest {

  private ClipboardCSVFinancialRecordExporter exporter;

  @BeforeEach
  void setUp() {
    exporter = new ClipboardCSVFinancialRecordExporter();
  }

  @Test
  void testExport() throws IOException, UnsupportedFlavorException {
    // given
    var records = List.of(
        new FinancialFormattedRecord("12/9/2023", "COMPRA EN  MERCADOPAG", "278500.00",
            "Bancolombia"),
        new FinancialFormattedRecord("12/10/2023", "COMPRA EN  MERCADO PA", "170000.00",
            "Bancolombia"),
        new FinancialFormattedRecord("12/10/2023", "COMPRA EN  TIENDA D1", "67910.00",
            "Bancolombia"));

    // when
    exporter.export(records);

    // then
    String clipboardContent = (String) Toolkit.getDefaultToolkit()
        .getSystemClipboard()
        .getData(DataFlavor.stringFlavor);

    assertThat(clipboardContent).isNotEmpty();
    String[] split = clipboardContent.split("\n");
    assertThat(split).hasSize(3);
    assertThat(clipboardContent).contains("12/9/2023\tCOMPRA EN  MERCADOPAG\t278500.00\tBancolombia");
    assertThat(clipboardContent).contains("12/10/2023\tCOMPRA EN  MERCADO PA\t170000.00\tBancolombia");
    assertThat(clipboardContent).contains("12/10/2023\tCOMPRA EN  TIENDA D1\t67910.00\tBancolombia");

  }
}
