package com.garodriguezlp.irimo.service;

import com.garodriguezlp.irimo.domain.FinancialRecord;
import com.garodriguezlp.irimo.domain.FormattedFinancialRecord;
import com.garodriguezlp.irimo.exporter.FinancialRecordExporter;
import com.garodriguezlp.irimo.extractor.FinancialRecordExtractor;
import com.garodriguezlp.irimo.formatter.FinancialRecordFormatter;
import com.garodriguezlp.irimo.service.exception.FinancialRecordProcessingException;
import java.io.File;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FinancialRecordsConsolidationService {

  private static final Logger LOGGER = LoggerFactory.getLogger(
      FinancialRecordsConsolidationService.class);

  private final FinancialRecordExtractorProvider extractorProvider;
  private final FinancialRecordFormatter formatter;
  private final FinancialRecordExporterProvider exporterProvider;

  public FinancialRecordsConsolidationService(FinancialRecordExtractorProvider extractorProvider,
      FinancialRecordFormatter formatter,
      FinancialRecordExporterProvider exporterProvider) {
    this.extractorProvider = extractorProvider;
    this.formatter = formatter;
    this.exporterProvider = exporterProvider;
  }

  public void processRecords(List<File> files, String sourceId, String targetId) {
    LOGGER.info("Starting consolidation and export process for source: {} and target: {}", sourceId,
        targetId);

    try {
      List<FinancialRecord> records = extractRecords(files, sourceId);
      List<FormattedFinancialRecord> formattedRecords = formatRecords(records);
      exportRecords(formattedRecords, targetId);
    } catch (Exception e) {
      throw new FinancialRecordProcessingException(
          "Failed to consolidate and export financial records", e);
    }
  }

  private List<FinancialRecord> extractRecords(List<File> files, String sourceId) {
    FinancialRecordExtractor extractor = extractorProvider.get(sourceId);
    LOGGER.debug("Using extractor: {}", extractor.getClass().getSimpleName());
    List<FinancialRecord> records = extractor.extract(files);
    LOGGER.info("Extracted {} records from {} files", records.size(), files.size());
    return records;
  }

  private List<FormattedFinancialRecord> formatRecords(List<FinancialRecord> records) {
    List<FormattedFinancialRecord> formattedRecords = formatter.format(records);
    LOGGER.debug("Formatted {} records", formattedRecords.size());
    return formattedRecords;
  }

  private void exportRecords(List<FormattedFinancialRecord> formattedRecords, String targetId) {
    FinancialRecordExporter exporter = exporterProvider.get(targetId);
    LOGGER.debug("Using exporter: {}", exporter.getClass().getSimpleName());
    exporter.export(formattedRecords);
    LOGGER.info("Successfully exported {} formatted records", formattedRecords.size());
  }
}
