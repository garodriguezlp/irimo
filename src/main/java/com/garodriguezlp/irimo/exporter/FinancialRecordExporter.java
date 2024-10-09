package com.garodriguezlp.irimo.exporter;

import com.garodriguezlp.irimo.domain.FormattedFinancialRecord;
import com.garodriguezlp.irimo.service.Identifiable;
import java.util.List;

public interface FinancialRecordExporter extends Identifiable {

  void export(List<FormattedFinancialRecord> financialRecords);

}
