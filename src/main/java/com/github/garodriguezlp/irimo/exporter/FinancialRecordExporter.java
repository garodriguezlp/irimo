package com.github.garodriguezlp.irimo.exporter;

import com.github.garodriguezlp.irimo.domain.FormattedFinancialRecord;
import com.github.garodriguezlp.irimo.service.Identifiable;
import java.util.List;

public interface FinancialRecordExporter extends Identifiable {

  void export(List<FormattedFinancialRecord> financialRecords);

}
