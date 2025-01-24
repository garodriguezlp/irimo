package com.github.garodriguezlp.irimo.extractor.ocr.parser;

import com.github.garodriguezlp.irimo.domain.FinancialRecord;
import java.util.List;

public interface FinancialRecordParser {

  List<FinancialRecord> extractRecords(String ocrText);

}
