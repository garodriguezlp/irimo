package com.github.garodriguezlp.irimo.exporter;

import com.github.garodriguezlp.irimo.domain.FormattedFinancialRecord;
import com.github.garodriguezlp.irimo.exporter.writer.CSVFinancialRecordsWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

// @todo: test me!!
@Service
public class CSVFinancialRecordExporter implements FinancialRecordExporter {

  private static final Logger LOGGER = LoggerFactory.getLogger(CSVFinancialRecordExporter.class);
  private final CSVFinancialRecordsWriter csvWriter;

  public CSVFinancialRecordExporter(CSVFinancialRecordsWriter csvWriter) {
    this.csvWriter = csvWriter;
  }

  @Override
  public void export(List<FormattedFinancialRecord> financialRecords) {
    String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    String fileName = "irimo-" + timestamp + ".csv";
    File file = new File(fileName);

    try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file))) {
      LOGGER.debug("Starting export of {} financial records", financialRecords.size());
      csvWriter.write(writer, financialRecords);
      LOGGER.debug("Finished export of financial records");
      LOGGER.debug("Finished export of financial records");
      LOGGER.info("Financial records exported to file: {}", file.getAbsolutePath());
    } catch (IOException e) {
      LOGGER.error("Error writing financial records to file", e);
    }
  }

  @Override
  public String getIdentifier() {
    return "csv";
  }
}
