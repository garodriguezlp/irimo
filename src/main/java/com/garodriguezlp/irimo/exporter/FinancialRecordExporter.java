package com.garodriguezlp.irimo.exporter;

import com.garodriguezlp.irimo.domain.FormattedFinancialRecord;
import java.util.List;

public interface FinancialRecordExporter {

  void export(List<FormattedFinancialRecord> financialRecords);

}
