package com.github.garodriguezlp.irimo.exporter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.github.garodriguezlp.irimo.domain.FormattedFinancialRecord;
import com.github.garodriguezlp.irimo.exporter.clipboard.ClipboardService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClipboardCSVFinancialRecordExporterTest {

  @Mock
  private ClipboardService clipboard;

  @InjectMocks
  private ClipboardCSVFinancialRecordExporter exporter;

  @Captor
  private ArgumentCaptor<String> clipboardContentCaptor;

  @Test
  void testExport() {
    // given
    var records = List.of(
        new FormattedFinancialRecord("12/9/2023", "COMPRA EN  MERCADOPAG", "278500.00", "Bancolombia"),
        new FormattedFinancialRecord("12/10/2023", "COMPRA EN  MERCADO PA", "170000.00", "Bancolombia"),
        new FormattedFinancialRecord("12/10/2023", "COMPRA EN  TIENDA D1", "67910.00", "Bancolombia")
    );

    // when
    exporter.export(records);

    // then
    verify(clipboard).copy(clipboardContentCaptor.capture());
    String clipboardContent = clipboardContentCaptor.getValue();

    assertThat(clipboardContent).isNotEmpty();
    String[] split = clipboardContent.split("\n");
    assertThat(split).hasSize(3);
    assertThat(clipboardContent).contains("12/9/2023\tCOMPRA EN  MERCADOPAG\t278500.00\tBancolombia");
    assertThat(clipboardContent).contains("12/10/2023\tCOMPRA EN  MERCADO PA\t170000.00\tBancolombia");
    assertThat(clipboardContent).contains("12/10/2023\tCOMPRA EN  TIENDA D1\t67910.00\tBancolombia");
  }
}
