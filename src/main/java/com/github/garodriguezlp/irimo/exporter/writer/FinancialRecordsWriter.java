package com.github.garodriguezlp.irimo.exporter.writer;

import com.github.garodriguezlp.irimo.domain.FormattedFinancialRecord;
import java.util.List;

public interface FinancialRecordsWriter {

  void write(Appendable out, List<FormattedFinancialRecord> financialRecords);

}
