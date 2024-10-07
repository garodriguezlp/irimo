package com.garodriguezlp.irimo.extractor;

import com.garodriguezlp.irimo.domain.FinancialRecord;
import java.io.File;
import java.util.List;

public interface FinancialRecordExtractor {

  List<FinancialRecord> extract(File dataDirectory);
}
