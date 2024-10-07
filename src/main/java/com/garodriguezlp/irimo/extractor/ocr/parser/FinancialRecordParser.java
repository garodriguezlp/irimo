package com.garodriguezlp.irimo.extractor.ocr.parser;

import com.garodriguezlp.irimo.domain.FinancialRecord;
import java.util.List;

public interface FinancialRecordParser {

  List<FinancialRecord> extractRecords(String ocrText);

}
