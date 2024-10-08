package com.garodriguezlp.irimo.exporter;

import static org.apache.commons.csv.CSVFormat.TDF;

import com.garodriguezlp.irimo.domain.FormattedFinancialRecord;
import com.garodriguezlp.irimo.exporter.exception.ExporterException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ClipboardCSVFinancialRecordExporter implements FinancialRecordExporter {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClipboardCSVFinancialRecordExporter.class);

  @Override
  public void export(List<FormattedFinancialRecord> financialRecords) {
    LOGGER.info("Starting export of {} financial records to clipboard", financialRecords.size());
    try {
      String csvData = convertRecordsToCSV(financialRecords);
      copyToClipboard(csvData);
      LOGGER.info("Successfully exported financial records to clipboard");
    } catch (Exception e) {
      LOGGER.error("Failed to export financial records", e);
      throw new ExporterException("Failed to export financial records", e);
    }
  }

  private String convertRecordsToCSV(List<FormattedFinancialRecord> records) throws IOException {
    LOGGER.debug("Converting {} records to CSV format", records.size());
    try (StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, TDF)) {
      for (FormattedFinancialRecord record : records) {
        printer.printRecord(
            record.date(),
            record.description(),
            record.amount(),
            record.source()
        );
      }
      return writer.toString();
    }
  }

  private void copyToClipboard(String data) {
    LOGGER.debug("Copying data to clipboard");
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    StringSelection selection = new StringSelection(data);
    clipboard.setContents(selection, null);
  }
}
