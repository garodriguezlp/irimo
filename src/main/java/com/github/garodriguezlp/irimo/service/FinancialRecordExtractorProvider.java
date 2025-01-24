package com.github.garodriguezlp.irimo.service;

import com.github.garodriguezlp.irimo.extractor.FinancialRecordExtractor;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FinancialRecordExtractorProvider extends ServiceProvider<FinancialRecordExtractor> {

  public FinancialRecordExtractorProvider(List<FinancialRecordExtractor> extractors) {
    super(extractors);
  }
}
