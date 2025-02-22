package com.github.garodriguezlp.irimo.exporter;

import com.github.garodriguezlp.irimo.domain.FormattedFinancialRecord;
import com.github.garodriguezlp.irimo.exporter.writer.CSVFinancialRecordsWriter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SystemOutCSVFinancialRecordExporter implements FinancialRecordExporter {

  private static final Logger LOGGER = LoggerFactory.getLogger(SystemOutCSVFinancialRecordExporter.class);
  private final CSVFinancialRecordsWriter csvWriter;

  public SystemOutCSVFinancialRecordExporter(CSVFinancialRecordsWriter csvWriter) {
    this.csvWriter = csvWriter;
  }

  @Override
  public void export(List<FormattedFinancialRecord> financialRecords) {
    LOGGER.debug("Starting export of {} financial records", financialRecords.size());
    csvWriter.write(System.out, financialRecords);
    LOGGER.debug("Finished export of financial records");
  }

  @Override
  public String getIdentifier() {
    return "out";
  }
}
