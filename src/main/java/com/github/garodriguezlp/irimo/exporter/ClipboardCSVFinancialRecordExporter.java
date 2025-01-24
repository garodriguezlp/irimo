package com.github.garodriguezlp.irimo.exporter;

import static org.apache.commons.csv.CSVFormat.TDF;

import com.github.garodriguezlp.irimo.domain.FormattedFinancialRecord;
import com.github.garodriguezlp.irimo.exporter.clipboard.ClipboardService;
import com.github.garodriguezlp.irimo.exporter.exception.ExporterException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ClipboardCSVFinancialRecordExporter implements FinancialRecordExporter {

  private static final Logger LOGGER = LoggerFactory.getLogger(
      ClipboardCSVFinancialRecordExporter.class);

  private final ClipboardService clipboard;

  public ClipboardCSVFinancialRecordExporter(ClipboardService clipboard) {
    this.clipboard = clipboard;
  }

  @Override
  public void export(List<FormattedFinancialRecord> financialRecords) {
    LOGGER.debug("Starting export of {} financial records to clipboard", financialRecords.size());
    try {
      String csvData = convertRecordsToCSV(financialRecords);
      clipboard.copy(csvData);
      LOGGER.debug("Successfully exported financial records to clipboard");
    } catch (Exception e) {
      LOGGER.error("Failed to export financial records", e);
      throw new ExporterException("Failed to export financial records", e);
    }
  }

  @Override
  public String getIdentifier() {
    return "clipboard";
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
}
