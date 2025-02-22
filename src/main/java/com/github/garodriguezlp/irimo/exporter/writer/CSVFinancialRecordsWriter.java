package com.github.garodriguezlp.irimo.exporter.writer;

import com.github.garodriguezlp.irimo.domain.FormattedFinancialRecord;
import java.io.IOException;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

// @todo: test me!!
@Service
public class CSVFinancialRecordsWriter implements FinancialRecordsWriter {

  private static final Logger LOGGER = LoggerFactory.getLogger(CSVFinancialRecordsWriter.class);

  @Override
  public void write(Appendable out, List<FormattedFinancialRecord> financialRecords) {
    LOGGER.debug("Starting to write {} financial records to output", financialRecords.size());
    try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.TDF)) {
      for (FormattedFinancialRecord record : financialRecords) {
        printer.printRecord(
            record.date(),
            record.description(),
            record.amount(),
            record.source()
        );
      }
      LOGGER.debug("Successfully wrote financial records to output");
    } catch (IOException e) {
      LOGGER.error("Failed to write financial records", e);
      throw new RuntimeException("Failed to write financial records", e);
    }
  }
}
