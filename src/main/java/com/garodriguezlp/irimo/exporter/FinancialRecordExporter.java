package com.garodriguezlp.irimo.exporter;

import com.garodriguezlp.irimo.domain.FinancialFormattedRecord;
import java.util.List;

public interface FinancialRecordExporter {

  void export(List<FinancialFormattedRecord> financialRecords);

}
