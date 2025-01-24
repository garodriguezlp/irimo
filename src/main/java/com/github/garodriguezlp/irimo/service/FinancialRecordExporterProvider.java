package com.github.garodriguezlp.irimo.service;

import com.github.garodriguezlp.irimo.exporter.FinancialRecordExporter;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FinancialRecordExporterProvider extends ServiceProvider<FinancialRecordExporter> {

  public FinancialRecordExporterProvider(List<FinancialRecordExporter> extractors) {
    super(extractors);
  }
}
